import {DataTable} from "@/components/ui/data-table.tsx";
import {columns} from "@/features/admin/components/books/columns.tsx";
import {useQuery} from "@tanstack/react-query";
import {fetchAllBooks} from "@/services/book.service";
import {useState} from "react";

export default function BookTable() {
    const [search, setSearch] = useState("");
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);

    const { data, isLoading, isError } = useQuery({
        queryKey: ["admin-books", search, page, size],
        queryFn: () => fetchAllBooks(search, page, size),
    });

    if (isLoading) {
        return <div className="container mx-auto py-10">Loading...</div>;
    }

    if (isError) {
        return <div className="container mx-auto py-10 text-red-500">Error loading books.</div>;
    }

    return (
        <div className="container mx-auto py-10">
            <DataTable columns={columns} data={data?.content ?? []} />
        </div>
    );
}