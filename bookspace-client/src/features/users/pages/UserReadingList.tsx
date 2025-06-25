import {Link, useParams} from "react-router";
import {DataTable} from "@/components/ui/data-table.tsx";
import {useQuery} from "@tanstack/react-query";
import {columns} from "@/features/users/components/columns.tsx";
import {fetchUserReadingList} from "@/services/reading-list.service.ts";


const UserReadingList = () => {

    let params = useParams();

    const { data, isLoading, isError } = useQuery({
        queryKey: ["reading-list", params.username],
        queryFn: () => fetchUserReadingList(params.username as string,0,100),
    });

    if (isLoading) {
        return <div className="container mx-auto py-10">Loading...</div>;
    }

    if (isError) {
        return <div className="container mx-auto py-10 text-red-500">Error loading books.</div>;
    }

    return (
        <div className={"min-h-screen"}>
            <div className="flex flex-col  justify-start items-start mb-4">
                <Link to={"reading-list"} className="text-3xl font-semibold hover:underline cursor-pointer">Reading List</Link>
                <span className={"text-ld text-muted-foreground"}>All Books</span>
            </div>
            <DataTable columns={columns} data={data?.content ?? []} />
            </div>
    );
};

export default UserReadingList;