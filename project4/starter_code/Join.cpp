#include "Join.hpp"
#include "Bucket.hpp"
// #include "P.hpp"
#include <functional>

unsigned int output_buffer = 14;
unsigned int input_buffer = 15;

bool logPartition = false;

void loadRef(
    Disk* disk, 
    Mem* mem, 
    vector<Bucket>& buckets,
    bool left, 
    unsigned int start,
    unsigned int end
);


void loadRef(
    Disk* disk, 
    Mem* mem, 
    vector<Bucket>& buckets,
    bool left, 
    unsigned int start,
    unsigned int end
) {
    if (logPartition) {
        std::cout << "===== LOADING REF ===== \n";
        std::cout << "FROM " << start << " TO " << end << "\n";
    }
    Page* input_buffer_page = mem->mem_page(input_buffer);
    for (unsigned int page_id = start; page_id < end; page_id++) {
        mem->loadFromDisk(disk, page_id, input_buffer);
        // input_buffer_page->print();

        for (unsigned int record_idx = 0; record_idx < input_buffer_page->size(); ++record_idx) {
            Record current_record = input_buffer_page->get_record(record_idx);
            if (logPartition) {
                current_record.print();
                std::cout << current_record.partition_hash() % MEM_SIZE_IN_PAGE - 1 << "\n"; 
            }
            unsigned int partition_index = current_record.partition_hash() % (MEM_SIZE_IN_PAGE - 1);
            Page* partition_buffer_page = mem->mem_page(partition_index);
            partition_buffer_page->loadRecord(current_record);
            if (partition_buffer_page->full()) {
                unsigned int disk_page_id = mem->flushToDisk(disk, partition_index);
                    if (logPartition) {
                        std::cout << "flushed to: " << disk_page_id << "\n"; 
                    }
                if (left) {
                    buckets[partition_index].add_left_rel_page(disk_page_id);
                } else {
                    buckets[partition_index].add_right_rel_page(disk_page_id);
                }
            }
        }
    }

    for (unsigned int partition_index = 0; partition_index < MEM_SIZE_IN_PAGE - 1; partition_index++) {
        Page* partition_buffer_page = mem->mem_page(partition_index);
        if (partition_buffer_page->size() > 0) {
            if (logPartition) {
                std::cout << "Wrote " << partition_index << " to disk\n";
            }
            unsigned int disk_page_id = mem->flushToDisk(disk, partition_index);
            if (left) {
                buckets[partition_index].add_left_rel_page(disk_page_id);
            } else {
                buckets[partition_index].add_right_rel_page(disk_page_id);
            }
        }

    }
}


/*
 * TODO: Student implementation
 * Input: Disk, Memory, Disk page ids for left relation, Disk page ids for right relation
 * Output: Vector of Buckets of size (MEM_SIZE_IN_PAGE - 1) after partition
 */
vector<Bucket> partition(
    Disk* disk, 
    Mem* mem, 
    pair<unsigned int, unsigned int> left_rel, 
    pair<unsigned int, unsigned int> right_rel) 
{
    vector<Bucket> buckets;
    for (unsigned int i = 0; i < MEM_SIZE_IN_PAGE - 1; i++) {
        buckets.push_back(Bucket(disk));
    }

    const unsigned int left_start = std::get<0>(left_rel);
    const unsigned int left_end = std::get<1>(left_rel);
    const unsigned int right_start = std::get<0>(right_rel);
    const unsigned int right_end = std::get<1>(right_rel);

    loadRef(disk, mem, buckets, true, left_start, left_end);
    mem->reset();
    loadRef(disk, mem, buckets, false, right_start, right_end);
    mem->reset();

    if (!logPartition) {
        return buckets;
    }

    for (unsigned int i = 0; i < MEM_SIZE_IN_PAGE - 1; i++) {
        std::cout << i << ", right: " << buckets[i].num_left_rel_record << ", left: " << buckets[i].num_right_rel_record << "\n";
    }

    return buckets;
}

/*
 * TODO: Student implementation
 * Input: Disk, Memory, Vector of Buckets after partition
 * Output: Vector of disk page ids for join result
 */
vector<unsigned int> probe(Disk* disk, Mem* mem, vector<Bucket>& partitions) {
    vector<unsigned int> page_ids;
    mem->reset();

    Page* output_buffer_page = mem->mem_page(output_buffer);
    // cout << "partition size: " << partitions.size() << endl;

    for (unsigned int i = 0; i < partitions.size(); i++) {
        Bucket partition = partitions[i];

        bool left_smaller = partition.num_left_rel_record < partition.num_right_rel_record;

        vector<unsigned int> smaller = left_smaller ? partition.get_left_rel() : partition.get_right_rel();
        vector<unsigned int> larger = left_smaller ? partition.get_right_rel() : partition.get_left_rel();
        // std::cout << i << ", right: " << partition.num_left_rel_record << ", left: " << partition.num_right_rel_record << "\n";

        for (auto j : smaller) {
            mem->loadFromDisk(disk, j, input_buffer);
            Page* input_buffer_page = mem->mem_page(input_buffer);
            for (unsigned int k = 0; k < input_buffer_page->size(); k++) {
                Record rec = input_buffer_page->get_record(k);
                unsigned int partition_index = rec.probe_hash() % (MEM_SIZE_IN_PAGE - 2);
                Page* partition_buffer_page = mem->mem_page(partition_index);
                partition_buffer_page->loadRecord(rec);
            }
            for (auto k : larger) {
                mem->loadFromDisk(disk, k, input_buffer);
                Page* input_buffer_page_check = mem->mem_page(input_buffer);
                for (unsigned int monke = 0; monke < input_buffer_page->size(); monke++) {
                    Record rec_big = input_buffer_page_check->get_record(monke);
                    unsigned int partition_index = rec_big.probe_hash() % (MEM_SIZE_IN_PAGE - 2);
                    // rec.print();
                    // cout << partition_index << "\n"; 

                    Page* hash_hit = mem->mem_page(partition_index);

                    for (unsigned int a = 0; a < hash_hit->size(); ++a) {
                        Record rec_small = hash_hit->get_record(a);
                        if (rec_big == rec_small) {
                            // cout << a << " hit " << "\n"; 
                            left_smaller ? output_buffer_page->loadPair(rec_big, rec_small) : output_buffer_page->loadPair(rec_small, rec_big);
                        }
                        if (output_buffer_page->full()) {
                            unsigned int disk_page_id = mem->flushToDisk(disk, output_buffer);
                            page_ids.push_back(disk_page_id);
                        }
                    }
                }
            }
        }
        mem->reset();
    }
    if (output_buffer_page->size() > 0) {
        unsigned int disk_page_id = mem->flushToDisk(disk, output_buffer);
        page_ids.push_back(disk_page_id);
    }

    return page_ids;
}

