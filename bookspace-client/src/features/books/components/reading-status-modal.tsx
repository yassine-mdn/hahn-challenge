import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { CreateReadingForm } from "./create-reading-form";
import { UpdateReadingForm } from "./update-reading-form";
import type { ReadingListDTO } from "@/types/reading-list-dto";

interface ReadingStatusModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  bookId: number;
  bookTitle: string;
  username: string;
  mode: "create" | "update";
  currentReadingItem?: ReadingListDTO | null;
}

export function ReadingStatusModal({
  open,
  onOpenChange,
  bookId,
  bookTitle,
  username,
  mode,
  currentReadingItem,
}: ReadingStatusModalProps) {
  const isCreating = mode === "create";

  const handleSuccess = () => {
    onOpenChange(false);
  };

  const handleCancel = () => {
    onOpenChange(false);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle>
            {isCreating ? "Add to Reading List" : "Update Reading Status"}
          </DialogTitle>
          <DialogDescription>
            {isCreating 
              ? `Add "${bookTitle}" to your reading list`
              : `Update your reading status for "${bookTitle}"`
            }
          </DialogDescription>
        </DialogHeader>

        {isCreating ? (
          <CreateReadingForm
            bookId={bookId}
            bookTitle={bookTitle}
            username={username}
            onSuccess={handleSuccess}
            onCancel={handleCancel}
          />
        ) : (
          currentReadingItem && (
            <UpdateReadingForm
              bookId={bookId}
              bookTitle={bookTitle}
              username={username}
              currentReadingItem={currentReadingItem}
              onSuccess={handleSuccess}
              onCancel={handleCancel}
            />
          )
        )}
      </DialogContent>
    </Dialog>
  );
} 