#include "Join.hpp"
#include "Bucket.hpp"
// #include "P.hpp"
#include <functional>

int mod_const = MEM_SIZE_IN_PAGE - 1;
int input_buffer = 15;



void loadRef(
    Disk* disk, 
    Mem* mem, 
    vector<Bucket>* buckets,
    bool left, 
    int start,
    int end
);


void loadRef(
    Disk* disk, 
    Mem* mem, 
    vector<Bucket> buckets,
    bool left, 
    int start,
    int end
) {
    cout << "===== LOADING REF ===== \n";
    Page* input_buffer_page = mem->mem_page(input_buffer);
    for (int page_id = start; page_id < end; page_id++) {
        mem->loadFromDisk(disk, page_id, input_buffer);
        // input_buffer_page->print();

        for (int record_idx = 0; record_idx < input_buffer_page->size(); ++record_idx) {
            Record current_record = input_buffer_page->get_record(record_idx);
            current_record.print();
            cout << current_record.partition_hash() % mod_const << "\n"; 
            int partition_index = current_record.partition_hash() % mod_const;
            Page* partition_buffer_page = mem->mem_page(partition_index);
            partition_buffer_page->loadRecord(current_record);
            if (partition_buffer_page->full()) {
                int disk_page_id = mem->flushToDisk(disk, partition_index);
                cout << "flushed to: " << disk_page_id << "\n"; 
                if (left) {
                    buckets[partition_index].add_left_rel_page(disk_page_id);
                } else {
                    buckets[partition_index].add_right_rel_page(disk_page_id);
                }
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
    for (int i = 0; i < MEM_SIZE_IN_PAGE - 1; i++) {
        buckets.push_back(Bucket(disk));
    }

    const int left_start = std::get<0>(left_rel);
    const int left_end = std::get<1>(left_rel);
    const int right_start = std::get<0>(right_rel);
    const int right_end = std::get<1>(right_rel);

    loadRef(disk, mem, buckets, true, left_start, left_end);
    mem->reset();
    loadRef(disk, mem, buckets, false, right_start, right_end);


    // std::cout << "memsize: " << MEM_SIZE_IN_PAGE << "\n";
    std::cout << '(' << left_start << ", " << left_end << ")\n";
    std::cout << '(' << right_start << ", " << right_end << ")\n";

    return buckets;
}

/*
 * TODO: Student implementation
 * Input: Disk, Memory, Vector of Buckets after partition
 * Output: Vector of disk page ids for join result
 */
vector<unsigned int> probe(Disk* disk, Mem* mem, vector<Bucket>& partitions) {}

