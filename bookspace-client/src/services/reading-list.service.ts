import type { ReadingListDTO } from "@/types/reading-list-dto";
import type { ReadingListRequestDTO } from "@/types/reading-list-request-dto";
import instance from "./axios";
import type {PageWrapper} from "@/types/page-wrapper.ts";

export const fetchReadingListItem = async (username: string, id: number): Promise<ReadingListDTO> =>
    (await instance.get(`/api/v1/users/${username}/reading-list/${id}`)).data;

export const updateReadingListItem = async (
    username: string,
    id: number,
    payload: ReadingListRequestDTO
): Promise<ReadingListDTO> =>
    (await instance.put(`/api/v1/users/${username}/reading-list/${id}`, payload)).data;

export const deleteReadingListItem = async (username: string, id: number): Promise<void> =>
    (await instance.delete(`/api/v1/users/${username}/reading-list/${id}`)).data;

export const fetchUserReadingList = async (username: string, page:number, size: number): Promise<PageWrapper<ReadingListDTO>> =>
    (await instance.get(`/api/v1/users/${username}/reading-list`, {
        params: {
            page: page,
            size: size
        }
    })).data;

export const addReadingListItem = async (
    username: string,
    payload: ReadingListRequestDTO
): Promise<ReadingListDTO> =>
    (await instance.post(`/api/v1/users/${username}/reading-list`, payload)).data;


export const fetchReadingListItemByBookId = async (
    username: string,
    bookId: number
): Promise<ReadingListDTO | null> =>
    (await instance.get(`/api/v1/users/${username}/reading-list/book/${bookId}`)).data;
