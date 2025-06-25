import { useState } from "react"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import type { UserDTO } from "@/types/user-dto"
import { MoreHorizontal, SquarePen, Trash2 } from "lucide-react"
import { Button } from "@/components/ui/button"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { EditUserModal } from "./edit-user-modal"
import { DeleteUserModal } from "./delete-user-modal"
import { updateUser, deleteUser } from "@/services/user.service"
import { toast } from "sonner"

interface UserActionsProps {
  user: UserDTO
}

export function UserActions({ user }: UserActionsProps) {
  const [editModalOpen, setEditModalOpen] = useState(false)
  const [deleteModalOpen, setDeleteModalOpen] = useState(false)
  const queryClient = useQueryClient()

  const updateUserMutation = useMutation({
    mutationFn: (userData: UserDTO) => updateUser(parseInt(userData.id!), userData),
    onSuccess: () => {
      toast.success("User updated successfully")
      setEditModalOpen(false)
      queryClient.invalidateQueries({ queryKey: ["admin-users"] })
    },
    onError: (error) => {
      toast.error("Failed to update user")
      console.error("Update user error:", error)
    },
  })

  const deleteUserMutation = useMutation({
    mutationFn: (username: string) => deleteUser(username),
    onSuccess: () => {
      toast.success("User deleted successfully")
      setDeleteModalOpen(false)
      queryClient.invalidateQueries({ queryKey: ["admin-users"] })
    },
    onError: (error) => {
      toast.error("Failed to delete user")
      console.error("Delete user error:", error)
    },
  })

  const handleEdit = (updatedUser: UserDTO) => {
    updateUserMutation.mutate(updatedUser)
  }

  const handleDelete = (username: string) => {
    deleteUserMutation.mutate(username)
  }

  return (
    <>
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button variant="ghost" className="h-8 w-8 p-0">
            <span className="sr-only">Open menu</span>
            <MoreHorizontal className="h-4 w-4" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end">
          <DropdownMenuLabel>Actions</DropdownMenuLabel>
          <DropdownMenuItem onClick={() => setEditModalOpen(true)}>
            <SquarePen className="mr-2 h-4 w-4" />
            Edit
          </DropdownMenuItem>
          <DropdownMenuSeparator />
          <DropdownMenuItem 
            onClick={() => setDeleteModalOpen(true)}
            className="text-destructive focus:text-destructive"
          >
            <Trash2 className="mr-2 h-4 w-4" />
            Delete
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>

      <EditUserModal
        user={user}
        open={editModalOpen}
        onOpenChange={setEditModalOpen}
        onSave={handleEdit}
        isLoading={updateUserMutation.isPending}
      />

      <DeleteUserModal
        user={user}
        open={deleteModalOpen}
        onOpenChange={setDeleteModalOpen}
        onConfirm={handleDelete}
        isLoading={deleteUserMutation.isPending}
      />
    </>
  )
} 