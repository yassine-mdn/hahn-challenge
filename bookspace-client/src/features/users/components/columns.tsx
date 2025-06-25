import type {ColumnDef} from "@tanstack/react-table";
import {Badge} from "@/components/ui/badge.tsx";
import {getReadingListStatusLabel, type ReadingListDTO, ReadingListDTOStatusEnum} from "@/types/reading-list-dto.ts";
import {Link} from "react-router";
import {Rating, RatingButton} from "@/components/ui/rating.tsx";
import {format} from "date-fns";

export const columns: ColumnDef<ReadingListDTO>[] = [
    {
        accessorKey: "coverUrl",
        header: "Cover",
        cell: ({ row }) => {
            const url = row.original.book?.coverUrl;
            return url ? (
                <img src={url} alt={row.original.book?.coverUrl} className="object-cover w-12 aspect-5/8 rounded-lg" />
            ) : (
                <div className={"rounded-lg w-12 aspect-5/8 bg-card"}/>
            );
        },
    },
    {
        accessorKey: "title",
        header: "Title",
        cell: ({ row }) => {
            return <Link to={`/book/${row.original.book?.id}`} className={"text-md hover:underline cursor-pointer"}>
                {row.original.book?.title}
            </Link>
        }
    },
    {
        accessorKey: "status",
        header: "Status",
        cell: ({ row }) => {
            return <Badge
                variant="secondary"
                className="rounded-full p-0.5 gap-2 px-3 py-1.5 font-normal mr-2"
            >
                {getReadingListStatusLabel(row.original.status as ReadingListDTOStatusEnum)}
            </Badge>
        }
    },
    {
        accessorKey: "startedAt",
        header: "Started",
        cell: ({ row }) => {
            const startedAt = row.original.startedAt;
            return startedAt ? (
                <span className="text-sm text-muted-foreground">
                    {format(new Date(startedAt), 'MMM dd, yyyy')}
                </span>
            ) : (
                <span className="text-sm text-muted-foreground">-</span>
            );
        }
    },
    {
        accessorKey: "completedAt",
        header: "Completed",
        cell: ({ row }) => {
            const completedAt = row.original.completedAt;
            return completedAt ? (
                <span className="text-sm text-muted-foreground">
                    {format(new Date(completedAt), 'MMM dd, yyyy')}
                </span>
            ) : (
                <span className="text-sm text-muted-foreground">-</span>
            );
        }
    },
    {
        accessorKey: "rating",
        header: "Rating",
        cell: ({ row }) => {
            return <Rating className={"text-primary"} defaultValue={row.original.rating} readOnly>
                {Array.from({length: 5}).map((_, index) => (
                    <RatingButton key={index}/>
                ))}
            </Rating>
        }
    },
];