import {DataTable} from "@/components/ui/data-table";
import {columns} from "@/features/admin/components/users/columns";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {createUser, fetchAllUsers} from "@/services/user.service";
import {useSearchParams} from "react-router";
import {Button} from "@/components/ui/button";
import {Plus} from "lucide-react";
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious,
} from "@/components/ui/pagination";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {CreateUserModal} from "@/features/admin/components/users/create-user-modal";
import {toast} from "sonner";
import type {UserDTO} from "@/types/user-dto";
import {useState} from "react";

export default function UserTable() {
    const [searchParams, setSearchParams] = useSearchParams();
    const queryClient = useQueryClient();
    
    // Modal state
    const [createModalOpen, setCreateModalOpen] = useState(false);
    
    // Get state from URL
    const page = parseInt(searchParams.get('page') || '0');
    const size = parseInt(searchParams.get('size') || '10');

    const { data, isLoading, isError } = useQuery({
        queryKey: ["admin-users", page, size],
        queryFn: () => fetchAllUsers(page, size),
    });

    const createUserMutation = useMutation({
        mutationFn: (userData: UserDTO) => createUser(userData),
        onSuccess: () => {
            toast.success("User created successfully");
            setCreateModalOpen(false);
            queryClient.invalidateQueries({ queryKey: ["admin-users"] });
        },
        onError: (error) => {
            toast.error("Failed to create user");
            console.error("Create user error:", error);
        },
    });

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

    const handleNewUser = () => {
        setCreateModalOpen(true);
    };

    const handleCreateUser = (userData: UserDTO) => {
        createUserMutation.mutate(userData);
    };

    if (isError) {
        return <div className="container mx-auto py-10 text-red-500">Error loading users.</div>;
    }

    const totalPages = data?.totalPages || 0;
    const currentPage = page;

    return (
        <div className="container mx-auto py-10">
            <div className="flex flex-col gap-4 mb-6">
                <div className="flex flex-col sm:flex-row gap-4 items-start sm:items-center justify-between">
                    <h1 className="text-2xl font-bold">Users Management</h1>
                </div>
                
                <div className="flex gap-2 justify-end">
                    <Button onClick={handleNewUser} className="flex items-center gap-2">
                        <Plus className="h-4 w-4" />
                        New User
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

            {/* Create User Modal */}
            <CreateUserModal
                open={createModalOpen}
                onOpenChange={setCreateModalOpen}
                onSave={handleCreateUser}
                isLoading={createUserMutation.isPending}
            />
        </div>
    );
} 