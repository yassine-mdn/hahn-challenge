export interface PageWrapper<T>{
    totalPages: number;
    totalElements: number;
    hasNext: boolean;
    hasPrevious: boolean;
    content: Array<T>;
}