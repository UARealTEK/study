CREATE TABLE book_entity (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(255),
                             author VARCHAR(255)
                         );

CREATE TABLE user_entity (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             age INT,
                             full_name VARCHAR(100),
                             gender ENUM('MALE','FEMALE')
                         );

CREATE TABLE borrow_record_entity (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                      borrowed_at DATETIME(6),
                                      returned_at DATETIME(6),

                                      user_id BIGINT,
                                      book_id BIGINT,

                                      CONSTRAINT fk_borrow_user
                                          FOREIGN KEY (user_id) REFERENCES user_entity(id),

                                      CONSTRAINT fk_borrow_book
                                          FOREIGN KEY (book_id) REFERENCES book_entity(id)
                                  );