import type {BookDTO} from "@/types/book-dto";
import type {RatingCountDTO} from "@/types/rating-count-dto";
import instance from "./axios";
import type {PageWrapper} from "@/types/page-wrapper.ts";

export const fetchBookDetails = async (id: number): Promise<BookDTO> =>
  (await instance.get(`api/v1/books/${id}`)).data;

export const fetchSimilarBooks = async (id: number): Promise<PageWrapper<BookDTO>> =>
  (await instance.get(`api/v1/books/${id}/similar`)).data;

export const fetchRatings = async (id: number): Promise<RatingCountDTO[]> =>
  (await instance.get(`api/v1/books/${id}/ratings`)).data;

export const fetchPopularBooks = async () : Promise<PageWrapper<BookDTO>> =>
    (await instance.get(`api/v1/books/popular`)).data;

export const fetchFeaturedBooks = async () : Promise<PageWrapper<BookDTO>> =>
    (await instance.get(`api/v1/books/featured`)).data;

export const fetchAllBooks = async (keywork:string, pageNumber: number, pageSize:number): Promise<PageWrapper<BookDTO>> =>
    (await instance.get(`api/v1/books`, {
        params: {
            search: keywork,
            page: pageNumber,
            size: pageSize,
        }
    })).data;

// Admin operations
export const updateBook = async (id: number, bookData: BookDTO): Promise<BookDTO> =>
  (await instance.put(`api/v1/books/${id}`, bookData)).data;

export const deleteBook = async (id: number): Promise<void> =>
  (await instance.delete(`api/v1/books/${id}`)).data;
