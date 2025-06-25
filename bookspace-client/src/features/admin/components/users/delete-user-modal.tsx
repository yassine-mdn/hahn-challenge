import type { UserDTO } from "@/types/user-dto"
import { Button } from "@/components/ui/button"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"

interface DeleteUserModalProps {
  user: UserDTO | null
  open: boolean
  onOpenChange: (open: boolean) => void
  onConfirm: (username: string) => void
  isLoading?: boolean
}

export function DeleteUserModal({ user, open, onOpenChange, onConfirm, isLoading = false }: DeleteUserModalProps) {
  const handleConfirm = () => {
    if (user?.username) {
      onConfirm(user.username)
    }
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Delete User</DialogTitle>
          <DialogDescription>
            Are you sure you want to delete user "{user?.username}"? This action cannot be undone.
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