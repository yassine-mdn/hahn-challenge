import BookCard from "@/features/books/components/book-card.tsx";
import {useQuery} from "@tanstack/react-query";
import {fetchAllBooks} from "@/services/book.service.ts";
import {useSearchParams} from "react-router";
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious,
} from "@/components/ui/pagination";
import Loading from "@/components/ui/loading.tsx";

const BrowseBooks = () => {

    const [searchParams, setSearchParams] = useSearchParams();
    const search = searchParams.get('q') || "";
    const page = parseInt(searchParams.get('page') || '0');
    const size = 16;

    const books = useQuery({
        queryKey: ["books", search, page, size],
        queryFn: () => fetchAllBooks(search, page, size),
    });

    const handlePageChange = (newPage: number) => {
        const params = new URLSearchParams(searchParams);
        params.set('page', newPage.toString());
        setSearchParams(params);
    };

    if (books.isLoading) {
        return (
            <Loading/>
        )
    }

    if (books.isError) {
        return (
            <div className="h-full w-full grid place-content-center">
                <div className="text-center">
                    <h2 className="text-2xl font-bold text-red-600">Error loading books</h2>
                    <p className="text-muted-foreground">Please try again later.</p>
                </div>
            </div>
        )
    }

    const totalPages = books.data?.totalPages || 0;
    const currentPage = page;

    return (
        <div className="min-h-screen flex flex-col">
            <div className="flex-1">
                <div className="grid place-items-center grid-cols-2 sm:grid-cols-4 md:grid-cols-5 lg:grid-cols-6 gap-4 pb-4 mx-auto max-w-full overflow-x-hidden">
                    {books.isSuccess && books.data.content.map((book) => (
                        <BookCard
                            key={book.id}
                            id={book.id}
                            title={book.title}
                            author={book.author}
                            publisher={book.publisher}
                            genres={book.genres}
                            description={book.description}
                            coverUrl={book.coverUrl}
                        />
                    ))}
                </div>
            </div>
            
            {books.isSuccess && totalPages > 1 && (
                <div className="py-8">
                    <Pagination>
                        <PaginationContent>
                            <PaginationItem>
                                <PaginationPrevious 
                                    onClick={() => handlePageChange(Math.max(0, currentPage - 1))}
                                    className={currentPage === 0 ? "pointer-events-none opacity-50" : "cursor-pointer"}
                                />
                            </PaginationItem>
                            
                            {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
                                let pageNum;
                                if (totalPages <= 5) {
                                    pageNum = i;
                                } else if (currentPage < 3) {
                                    pageNum = i;
                                } else if (currentPage >= totalPages - 3) {
                                    pageNum = totalPages - 5 + i;
                                } else {
                                    pageNum = currentPage - 2 + i;
                                }
                                
                                return (
                                    <PaginationItem key={pageNum}>
                                        <PaginationLink
                                            onClick={() => handlePageChange(pageNum)}
                                            isActive={currentPage === pageNum}
                                            className="cursor-pointer"
                                        >
                                            {pageNum + 1}
                                        </PaginationLink>
                                    </PaginationItem>
                                );
                            })}
                            
                            <PaginationItem>
                                <PaginationNext 
                                    onClick={() => handlePageChange(Math.min(totalPages - 1, currentPage + 1))}
                                    className={currentPage === totalPages - 1 ? "pointer-events-none opacity-50" : "cursor-pointer"}
                                />
                            </PaginationItem>
                        </PaginationContent>
                    </Pagination>
                </div>
            )}
        </div>
    );
};

export default BrowseBooks;