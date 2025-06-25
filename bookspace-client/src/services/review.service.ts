import type { ReviewDTO } from "@/types/review-dto";
import type { ReviewRequestDTO } from "@/types/review-request-dto";
import instance from "./axios";
import type {PageWrapper} from "@/types/page-wrapper.ts";

export const createReview = async (payload: ReviewRequestDTO): Promise<ReviewDTO> =>
    (await instance.post(`/api/v1/reviews`, payload)).data;

export const fetchUserReviews = async (username: string): Promise<PageWrapper<ReviewDTO>> =>
    (await instance.get(`/api/v1/reviews/users/${username}`)).data;

export const fetchBookReviews = async (bookId: number): Promise<PageWrapper<ReviewDTO>> =>
    (await instance.get(`/api/v1/reviews/books/${bookId}`)).data;

export const deleteReview = async (reviewId: number): Promise<void> =>
    (await instance.delete(`/api/v1/reviews/${reviewId}`));