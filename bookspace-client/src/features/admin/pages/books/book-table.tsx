import {DataTable} from "@/components/ui/data-table.tsx";
import {columns} from "@/features/admin/components/books/columns.tsx";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {createBook, fetchAllBooks} from "@/services/book.service";
import {useSearchParams} from "react-router";
import {useEffect, useState} from "react";
import {Button} from "@/components/ui/button.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Plus, Search} from "lucide-react";
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious,
} from "@/components/ui/pagination";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue,} from "@/components/ui/select";
import {CreateBookModal} from "@/features/admin/components/books/create-book-modal.tsx";
import {toast} from "sonner";
import type {BookDTO} from "@/types/book-dto";

export default function BookTable() {
    const [searchParams, setSearchParams] = useSearchParams();
    const queryClient = useQueryClient();
    
    // Modal state
    const [createModalOpen, setCreateModalOpen] = useState(false);
    
    // Get state from URL
    const search = searchParams.get('search') || "";
    const page = parseInt(searchParams.get('page') || '0');
    const size = parseInt(searchParams.get('size') || '10');

    // Local state for search input
    const [searchValue, setSearchValue] = useState(search);

    // Update local search value when URL changes
    useEffect(() => {
        setSearchValue(search);
    }, [search]);

    const { data, isLoading, isError } = useQuery({
        queryKey: ["admin-books", search, page, size],
        queryFn: () => fetchAllBooks(search, page, size),
    });

    // Create book mutation
    const createBookMutation = useMutation({
        mutationFn: (bookData: BookDTO) => createBook(bookData),
        onSuccess: () => {
            toast.success("Book created successfully");
            setCreateModalOpen(false);
            queryClient.invalidateQueries({ queryKey: ["admin-books"] });
        },
        onError: (error) => {
            toast.error("Failed to create book");
            console.error("Create book error:", error);
        },
    });

    const handleSearch = (e: React.FormEvent) => {
        e.preventDefault();
        const params = new URLSearchParams(searchParams);
        if (searchValue.trim()) {
            params.set('search', searchValue.trim());
        } else {
            params.delete('search');
        }
        params.set('page', '0'); // Reset to first page when searching
        setSearchParams(params);
    };

    const handlePageChange = (newPage: number) => {
        const params = new URLSearchParams(searchParams);
        params.set('page', newPage.toString());
        setSearchParams(params);
    };

    const handleSizeChange = (newSize: string) => {
        const params = new URLSearchParams(searchParams);
        params.set('size', newSize);
        params.set('page', '0'); // Reset to first page when changing size
        setSearchParams(params);
    };

    const handleNewBook = () => {
        setCreateModalOpen(true);
    };

    const handleCreateBook = (bookData: BookDTO) => {
        createBookMutation.mutate(bookData);
    };

    if (isError) {
        return <div className="container mx-auto py-10 text-red-500">Error loading books.</div>;
    }

    const totalPages = data?.totalPages || 0;
    const currentPage = page;

    return (
        <div className="container mx-auto py-10">
            <div className="flex flex-col gap-4 mb-6">
                <div className="flex flex-col sm:flex-row gap-4 items-start sm:items-center justify-between">
                    <h1 className="text-2xl font-bold">Books Management</h1>

                </div>
                
                <div className={"flex gap-2 justify-end"}>
                    <form onSubmit={handleSearch}>
                        <div className="relative flex-1 md:w-md">
                            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
                            <Input
                                type="text"
                                placeholder="Search books..."
                                value={searchValue}
                                onChange={(e) => setSearchValue(e.target.value)}
                                className="pl-10"
                            />
                        </div>
                    </form>
                    <Button onClick={handleNewBook} className="flex items-center gap-2">
                        <Plus className="h-4 w-4" />
                        New Book
                    </Button>
                </div>
            </div>
            
            <DataTable
                columns={columns} 
                data={data?.content ?? []}
                isLoading={isLoading}
            />

            {/* Pagination */}
            <div className="flex items-center justify-between space-x-2 py-4">
                <div className="flex items-center space-x-2">
                    <p className="text-sm font-medium">Rows per page</p>
                    <Select value={size.toString()} onValueChange={handleSizeChange}>
                        <SelectTrigger className="h-8 w-[70px]">
                            <SelectValue placeholder={size} />
                        </SelectTrigger>
                        <SelectContent side="top">
                            {[10, 20, 30, 40, 50].map((pageSize) => (
                                <SelectItem key={pageSize} value={pageSize.toString()}>
                                    {pageSize}
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </div>
                
                <div className="flex items-center space-x-6 lg:space-x-8">
                    <div className="flex w-full items-center justify-center text-sm font-medium">
                        Page {currentPage + 1} of {totalPages}
                    </div>
                    <Pagination>
                        <PaginationContent>
                            <PaginationItem>
                                <PaginationPrevious 
                                    onClick={() => handlePageChange(Math.max(0, currentPage - 1))}
                                    className={currentPage === 0 ? "pointer-events-none opacity-50" : "cursor-pointer"}
                                />
                            </PaginationItem>
                            {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
                                const pageNum = Math.max(0, Math.min(totalPages - 1, currentPage - 2 + i));
                                return (
                                    <PaginationItem key={pageNum}>
                                        <PaginationLink
                                            onClick={() => handlePageChange(pageNum)}
                                            isActive={pageNum === currentPage}
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
                                    className={currentPage >= totalPages - 1 ? "pointer-events-none opacity-50" : "cursor-pointer"}
                                />
                            </PaginationItem>
                        </PaginationContent>
                    </Pagination>
                </div>
            </div>

            {/* Create Book Modal */}
            <CreateBookModal
                open={createModalOpen}
                onOpenChange={setCreateModalOpen}
                onSave={handleCreateBook}
                isLoading={createBookMutation.isPending}
            />
        </div>
    );
}