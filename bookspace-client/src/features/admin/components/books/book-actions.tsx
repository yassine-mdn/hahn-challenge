import { useState } from "react"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import type { BookDTO } from "@/types/book-dto"
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
import { EditBookModal } from "./edit-book-modal"
import { DeleteBookModal } from "./delete-book-modal"
import { updateBook, deleteBook } from "@/services/book.service"
import { toast } from "sonner"

interface BookActionsProps {
  book: BookDTO
}

export function BookActions({ book }: BookActionsProps) {
  const [editModalOpen, setEditModalOpen] = useState(false)
  const [deleteModalOpen, setDeleteModalOpen] = useState(false)
  const queryClient = useQueryClient()

  const updateBookMutation = useMutation({
    mutationFn: (bookData: BookDTO) => updateBook(bookData.id!, bookData),
    onSuccess: () => {
      toast.success("Book updated successfully")
      setEditModalOpen(false)
      queryClient.invalidateQueries({ queryKey: ["admin-books"] })
    },
    onError: (error) => {
      toast.error("Failed to update book")
      console.error("Update book error:", error)
    },
  })

  const deleteBookMutation = useMutation({
    mutationFn: (bookId: number) => deleteBook(bookId),
    onSuccess: () => {
      toast.success("Book deleted successfully")
      setDeleteModalOpen(false)
      queryClient.invalidateQueries({ queryKey: ["admin-books"] })
    },
    onError: (error) => {
      toast.error("Failed to delete book")
      console.error("Delete book error:", error)
    },
  })

  const handleEdit = (updatedBook: BookDTO) => {
    updateBookMutation.mutate(updatedBook)
  }

  const handleDelete = (bookId: number) => {
    deleteBookMutation.mutate(bookId)
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

      <EditBookModal
        book={book}
        open={editModalOpen}
        onOpenChange={setEditModalOpen}
        onSave={handleEdit}
        isLoading={updateBookMutation.isPending}
      />

      <DeleteBookModal
        book={book}
        open={deleteModalOpen}
        onOpenChange={setDeleteModalOpen}
        onConfirm={handleDelete}
        isLoading={deleteBookMutation.isPending}
      />
    </>
  )
} 