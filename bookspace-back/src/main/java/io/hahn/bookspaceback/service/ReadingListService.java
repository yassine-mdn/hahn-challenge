package io.hahn.bookspaceback.service;

import io.hahn.bookspaceback.dto.ReadingListDTO;
import io.hahn.bookspaceback.dto.ReadingListRequestDTO;
import io.hahn.bookspaceback.entity.Book;
import io.hahn.bookspaceback.entity.ReadingList;
import io.hahn.bookspaceback.entity.User;
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
            User user = userRepository.findById(readingListRequestDTO.getUserID()).orElseThrow(()-> {
                log.error("User with id {} not found", readingListRequestDTO.getUserID());
                return new CustomException("User with id " + readingListRequestDTO.getUserID() + " not found", HttpStatus.NOT_FOUND);
            });
            Book book = bookRepository.findById(readingListRequestDTO.getBookID()).orElseThrow(()-> {
                log.error("Book with id {} not found", readingListRequestDTO.getBookID());
                return new CustomException("Book with id " + readingListRequestDTO.getBookID() + " not found", HttpStatus.NOT_FOUND);
            });

            ReadingList saved = readingListRequestMapper.toEntity(readingListRequestDTO);
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

    public void delete(Long id) {
        try {
            readingListRepository.deleteById(id);
        } catch (Exception ex) {
            log.error("Error deleting readingList with id {} : {}", id, ex.getMessage());
            throw new CustomException("Failed to delete readingList with id " + id + " : " + ex);
        }
    }
}
