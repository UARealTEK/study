/*
 Enforces that borrowRecordDB contains only UNIQUE records
 of currently borrowed books
 (only one borrow record is able to exist in which specific book has returned_at == null)
 */

CREATE UNIQUE INDEX unique_active_borrow
    ON borrow_record (book_id)
    WHERE returned_at IS NULL;