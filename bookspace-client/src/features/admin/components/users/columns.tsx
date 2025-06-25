import type { ColumnDef } from "@tanstack/react-table";
import { Badge } from "@/components/ui/badge";
import { format } from "date-fns";
import type { UserDTO } from "@/types/user-dto";
import { UserDTORoleEnum } from "@/types/user-dto";
import { UserActions } from "./user-actions";

export const columns: ColumnDef<UserDTO>[] = [
    {
        accessorKey: "id",
        header: "ID",
        cell: ({ row }) => {
            return <span className="text-sm font-mono">{row.original.id}</span>;
        },
    },
    {
        accessorKey: "username",
        header: "Username",
        cell: ({ row }) => {
            return <span className="font-medium">{row.original.username}</span>;
        },
    },
    {
        accessorKey: "email",
        header: "Email",
        cell: ({ row }) => {
            return <span className="text-sm">{row.original.email}</span>;
        },
    },
    {
        accessorKey: "role",
        header: "Role",
        cell: ({ row }) => {
            const role = row.original.role as UserDTORoleEnum;
            return (
                <Badge
                    variant={role === "ADMIN" ? "default" : "secondary"}
                    className="rounded-full px-3 py-1.5 font-normal"
                >
                    {role}
                </Badge>
            );
        },
    },
    {
        accessorKey: "createdAt",
        header: "Created At",
        cell: ({ row }) => {
            const createdAt = row.original.createdAt;
            return createdAt ? (
                <span className="text-sm text-muted-foreground">
                    {format(new Date(createdAt), 'MMM dd, yyyy')}
                </span>
            ) : (
                <span className="text-sm text-muted-foreground">-</span>
            );
        },
    },
    {
        id: "actions",
        cell: ({ row }) => {
            return <UserActions user={row.original} />;
        },
    },
]; 