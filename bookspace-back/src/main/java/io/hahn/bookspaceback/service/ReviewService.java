package io.hahn.bookspaceback.service;

import io.hahn.bookspaceback.dto.ReviewDTO;
import io.hahn.bookspaceback.dto.ReviewRequestDTO;
import io.hahn.bookspaceback.entity.ReadingList;
import io.hahn.bookspaceback.entity.Review;
import io.hahn.bookspaceback.entity.enums.Status;
import io.hahn.bookspaceback.exception.CustomException;
import io.hahn.bookspaceback.mapper.ReviewMapper;
import io.hahn.bookspaceback.mapper.ReviewRequestMapper;
import io.hahn.bookspaceback.repository.ReadingListRepository;
import io.hahn.bookspaceback.repository.ReviewRepository;
import io.hahn.bookspaceback.util.PageWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    private final ReadingListRepository readingListRepository;
    private final ReviewRequestMapper reviewRequestMapper;
    private final ReviewMapper reviewMapper;
    
    public ReviewDTO create(ReviewRequestDTO reviewRequestDTO) {
        try{
            if (reviewRepository.existsByReadingList_Id(reviewRequestDTO.getReadingListId())) {
                log.error("Review already exists for ReadingList id {}", reviewRequestDTO.getReadingListId());
                throw new CustomException("Review already exists for ReadingList id " + reviewRequestDTO.getReadingListId());
            }
            ReadingList readingList = readingListRepository.findById(reviewRequestDTO.getReadingListId()).orElseThrow(() -> {
                log.error("ReadingList with id {} not found", reviewRequestDTO.getReadingListId());
                return new CustomException("ReadingList with id " + reviewRequestDTO.getReadingListId() + " not found", HttpStatus.NOT_FOUND);
            });
            if (readingList.getStatus() == Status.PLAN_TO_READ || readingList.getStatus() == Status.ON_HOLD) {
                log.error("Cannot add review: ReadingList status is {}", readingList.getStatus());
                throw new CustomException("Cannot add review for ReadingList with status " + readingList.getStatus(), HttpStatus.BAD_REQUEST);
            }            Review saved = reviewRequestMapper.toEntity(reviewRequestDTO);
            saved.setReadingList(readingList);
            saved =  reviewRepository.save(saved);
            return reviewMapper.toDTO(saved);
        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error creating review : {}", ex.getMessage(), ex);
            throw new CustomException("Failed to add review : " + ex);
        }
    }

    public PageWrapper<ReviewDTO> getAllByBookId(Long bookId, int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new CustomException("Invalid pagination parameters");
        }
        try {
            return new PageWrapper<>(reviewRepository.findAllByReadingList_Book_Id(bookId,PageRequest.of(pageNumber, pageSize)).map(reviewMapper::toDTO));
        } catch (Exception ex) {
            log.error("Error fetching reviews for bookId {}: {}", bookId, ex.getMessage());
            throw new CustomException("Failed to fetch reviews for bookId " + bookId + " : " + ex);
        }
    }

    public PageWrapper<ReviewDTO> getAllByUsername(String username, int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new CustomException("Invalid pagination parameters");
        }
        try {
            return new PageWrapper<>(reviewRepository.findAllByReadingList_User_UserName(username,PageRequest.of(pageNumber, pageSize)).map(reviewMapper::toDTO));
        } catch (Exception ex) {
            log.error("Error fetching reviews for username {} : {}", username, ex.getMessage());
            throw new CustomException("Failed to fetch reviews for username " + username + " : " + ex);
        }
    }

    public void delete(Long id) {
        try {
            reviewRepository.deleteById(id);
        } catch (Exception ex) {
            log.error("Error deleting review with id {} : {}", id, ex.getMessage());
            throw new CustomException("Failed to delete review with id " + id + " : " + ex);
        }
    }
}
