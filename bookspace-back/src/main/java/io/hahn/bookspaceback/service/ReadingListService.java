package io.hahn.bookspaceback.service;

import io.hahn.bookspaceback.dto.ReadingListDTO;
import io.hahn.bookspaceback.dto.ReadingListRequestDTO;
import io.hahn.bookspaceback.entity.Book;
import io.hahn.bookspaceback.entity.ReadingList;
import io.hahn.bookspaceback.entity.User;
import io.hahn.bookspaceback.entity.enums.Status;
import io.hahn.bookspaceback.exception.CustomException;
import io.hahn.bookspaceback.mapper.ReadingListMapper;
import io.hahn.bookspaceback.mapper.ReadingListRequestMapper;
import io.hahn.bookspaceback.repository.BookRepository;
import io.hahn.bookspaceback.repository.ReadingListRepository;
import io.hahn.bookspaceback.repository.UserRepository;
import io.hahn.bookspaceback.util.PageWrapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReadingListService {

    private final ReadingListRepository readingListRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReadingListMapper readingListMapper;
    private final ReadingListRequestMapper readingListRequestMapper;


    public ReadingListDTO create(ReadingListRequestDTO readingListRequestDTO) {
        try {
            User user = userRepository.findByUsername(readingListRequestDTO.getUsername()).orElseThrow(() -> {
                log.error("User with username {} not found", readingListRequestDTO.getUsername());
                return new CustomException("User with username " + readingListRequestDTO.getUsername() + " not found", HttpStatus.NOT_FOUND);
            });
            Book book = bookRepository.findById(readingListRequestDTO.getBookID()).orElseThrow(() -> {
                log.error("Book with id {} not found", readingListRequestDTO.getBookID());
                return new CustomException("Book with id " + readingListRequestDTO.getBookID() + " not found", HttpStatus.NOT_FOUND);
            });
            if (readingListRepository.existsByBook_IdAndUser_Id(readingListRequestDTO.getBookID(), readingListRequestDTO.getUsername())) {
                log.error("Book with id {} already in {} ReadingList", readingListRequestDTO.getBookID(), readingListRequestDTO.getUsername());
                throw new CustomException("Book with id " + readingListRequestDTO.getBookID() + "is already part of " + readingListRequestDTO.getUsername() + " reading list");
            }
            ReadingList saved = readingListRequestMapper.toEntity(readingListRequestDTO);
            applyStatusTimestamps(saved, readingListRequestDTO.getStatus());
            saved.setBook(book);
            saved.setUser(user);
            saved = readingListRepository.save(saved);
            return readingListMapper.toDTO(saved);
        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error creating readingList : {}", ex.getMessage(), ex);
            throw new CustomException("Failed to create readingList : " + ex);
        }
    }

    public ReadingListDTO update(Long id, ReadingListRequestDTO readingListRequestDTO) {
        try {
            ReadingList updatedReadingList = readingListRepository.findById(id)
                    .map(readingList -> {
                        readingListRequestMapper.updateReadingListFromDTO(readingListRequestDTO, readingList);
                        applyStatusTimestamps(readingList, readingListRequestDTO.getStatus());
                        return readingList;
                    })
                    .orElseThrow(() -> {
                        log.error("ReadingList with id {} not found", id);
                        return new CustomException("ReadingList with id " + id + " not found", HttpStatus.NOT_FOUND);
                    });

            updatedReadingList = readingListRepository.save(updatedReadingList);
            return readingListMapper.toDTO(updatedReadingList);
        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error updating readingList with id {} : {}", id, ex.getMessage(), ex);
            throw new CustomException("Failed to update readingList with id " + id + " : " + ex);
        }
    }

    public ReadingListDTO getById(Long id) {
        try {
            return readingListRepository.findById(id)
                    .map(readingListMapper::toDTO)
                    .orElseThrow(() -> {
                        log.error("ReadingList with id {} not found", id);
                        return new CustomException("ReadingList with id " + id + " not found", HttpStatus.NOT_FOUND);
                    });
        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error fetching readingList with id {}: {}", id, ex.getMessage(), ex);
            throw new CustomException("Failed to fetch readingList with id " + id + " : " + ex);
        }
    }//user

    public ReadingListDTO getByBookIdAndUserUsername(Long bookId, String username) {
        try {
            return readingListRepository.findByBook_IdAndUser_Username(bookId, username)
                    .map(readingListMapper::toDTO)
                    .orElseThrow(() -> {
                        log.error("ReadingList with associated book id {} and username {} not found", bookId, username);
                        return new CustomException("ReadingList with associated book id " + bookId + " and username " + username + " not found", HttpStatus.NOT_FOUND);
                    });
        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error fetching readingList for bookId {} and username {}: {}", bookId, username, ex.getMessage(), ex);
            throw new CustomException("Failed to fetch readingList for bookId " + bookId + " and username " + username + " : " + ex);
        }
    }

    public PageWrapper<ReadingListDTO> getAll(int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new CustomException("Invalid pagination parameters");
        }
        try {
            return new PageWrapper<>(readingListRepository.findAll(PageRequest.of(pageNumber, pageSize)).map(readingListMapper::toDTO));
        } catch (Exception ex) {
            log.error("Error fetching all readingLists : {}", ex.getMessage());
            throw new CustomException("Failed to fetch all readingLists : " + ex);
        }
    }

    public PageWrapper<ReadingListDTO> getAllByUser(String username, int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new CustomException("Invalid pagination parameters");
        }
        try {
            return new PageWrapper<>(readingListRepository.findAllByUser_Username(username, PageRequest.of(pageNumber, pageSize)).map(readingListMapper::toDTO));
        } catch (Exception ex) {
            log.error("Error fetching all readingLists for user {} : {}", username, ex.getMessage());
            throw new CustomException("Failed to fetch all readingLists : " + ex);
        }
    }

    public void delete(Long id) {
        try {
            readingListRepository.deleteById(id);
        } catch (Exception ex) {
            log.error("Error deleting readingList with id {} : {}", id, ex.getMessage());
            throw new CustomException("Failed to delete readingList with id " + id + " : " + ex);
        }
    }

    private void applyStatusTimestamps(ReadingList saved, Status status) {
        LocalDateTime now = LocalDateTime.now();

        switch (status) {
            case PLAN_TO_READ -> {
                saved.setStartedAt(null);
                saved.setCompletedAt(null);
            }
            case READING -> {
                if (saved.getStartedAt() == null) {
                    saved.setStartedAt(now);
                }
                saved.setCompletedAt(null);
            }
            case COMPLETED, DROPPED -> {
                if (saved.getStartedAt() == null) {
                    saved.setStartedAt(now);
                }
                if (saved.getCompletedAt() == null) {
                    saved.setCompletedAt(now);
                }
            }
            case ON_HOLD -> {
                if (saved.getStartedAt() == null) {
                    saved.setStartedAt(now);
                }
            }
        }
    }

}
