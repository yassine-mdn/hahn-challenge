import type { BookDTO } from "@/types/book-dto"
import { Button } from "@/components/ui/button"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"

interface DeleteBookModalProps {
  book: BookDTO | null
  open: boolean
  onOpenChange: (open: boolean) => void
  onConfirm: (bookId: number) => void
  isLoading?: boolean
}

export function DeleteBookModal({ book, open, onOpenChange, onConfirm, isLoading = false }: DeleteBookModalProps) {
  const handleConfirm = () => {
    if (book?.id) {
      onConfirm(book.id)
    }
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Delete Book</DialogTitle>
          <DialogDescription>
            Are you sure you want to delete "{book?.title}"? This action cannot be undone.
          </DialogDescription>
        </DialogHeader>
        <DialogFooter>
          <Button type="button" variant="outline" onClick={() => onOpenChange(false)} disabled={isLoading}>
            Cancel
          </Button>
          <Button type="button" variant="destructive" onClick={handleConfirm} disabled={isLoading}>
            {isLoading ? "Deleting..." : "Delete"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
} 