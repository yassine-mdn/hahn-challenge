import { useForm } from "react-hook-form";
import { useEffect } from "react";
import { Button } from "@/components/ui/button";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Rating, RatingButton } from "@/components/ui/rating";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { updateReadingListItem, deleteReadingListItem } from "@/services/reading-list.service";
import { ReadingListDTOStatusEnum, getReadingListStatusLabel } from "@/types/reading-list-dto";
import type { ReadingListRequestDTO } from "@/types/reading-list-request-dto";
import type { ReadingListDTO } from "@/types/reading-list-dto";
import {Trash2} from "lucide-react";

interface UpdateReadingFormProps {
  bookId: number;
  bookTitle: string;
  username: string;
  currentReadingItem: ReadingListDTO;
  onSuccess: () => void;
  onCancel: () => void;
}

interface FormData {
  status: ReadingListDTOStatusEnum;
  rating: number;
}

const readingStatusOptions = [
  ReadingListDTOStatusEnum.PLANTOREAD,
  ReadingListDTOStatusEnum.READING,
  ReadingListDTOStatusEnum.COMPLETED,
  ReadingListDTOStatusEnum.ONHOLD,
  ReadingListDTOStatusEnum.DROPPED,
];

export function UpdateReadingForm({
  bookId,
  username,
  currentReadingItem,
  onSuccess,
  onCancel,
}: UpdateReadingFormProps) {
  const queryClient = useQueryClient();

  const {
    handleSubmit,
    setValue,
    watch,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<FormData>({
    defaultValues: {
      status: currentReadingItem.status || ReadingListDTOStatusEnum.PLANTOREAD,
      rating: currentReadingItem.rating || 0,
    },
  });

  const watchedStatus = watch("status");
  const watchedRating = watch("rating");

  // Reset form when currentReadingItem changes
  useEffect(() => {
    reset({
      status: currentReadingItem.status || ReadingListDTOStatusEnum.PLANTOREAD,
      rating: currentReadingItem.rating || 0,
    });
  }, [currentReadingItem, reset]);

  const updateMutation = useMutation({
    mutationFn: (data: ReadingListRequestDTO) =>
      updateReadingListItem(username, currentReadingItem.id!, data),
    onSuccess: () => {
      toast.success("Reading list updated!");
      queryClient.invalidateQueries({ queryKey: ["userReadingItem"] });
      onSuccess();
    },
    onError: () => {
      toast.error("Failed to update reading list");
    },
  });

  const deleteMutation = useMutation({
    mutationFn: () => deleteReadingListItem(username, currentReadingItem.id!),
    onSuccess: () => {
      toast.success("Book removed from reading list!");
      queryClient.invalidateQueries({ queryKey: ["userReadingItem"] });
      onSuccess();
    },
    onError: () => {
      toast.error("Failed to remove book from reading list");
    },
  });

  const onSubmit = (data: FormData) => {
    const requestData: ReadingListRequestDTO = {
      bookID: bookId,
      status: data.status,
      rating: data.rating > 0 ? data.rating : undefined,
    };

    updateMutation.mutate(requestData);
  };

  const handleRemove = () => {
    deleteMutation.mutate();
  };

  const isLoading = isSubmitting || updateMutation.isPending || deleteMutation.isPending;

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="space-y-2">
        <label className="text-sm font-medium">Reading Status</label>
        <Select
          value={watchedStatus}
          onValueChange={(value) => setValue("status", value as ReadingListDTOStatusEnum)}
        >
          <SelectTrigger className="w-full">
            <SelectValue placeholder="Select reading status" />
          </SelectTrigger>
          <SelectContent>
            {readingStatusOptions.map((status) => (
              <SelectItem key={status} value={status}>
                {getReadingListStatusLabel(status)}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
        {errors.status && (
          <p className="text-sm text-red-500">{errors.status.message}</p>
        )}
      </div>

      <div className="flex flex-col space-y-2">
        <label className="text-sm font-medium">Rating</label>
        <Rating
          value={watchedRating}
          onValueChange={(value) => setValue("rating", value)}
          className={"text-primary"}
        >
          {Array.from({ length: 5 }).map((_, index) => (
            <RatingButton key={index} />
          ))}
        </Rating>
        {errors.rating && (
          <p className="text-sm text-red-500">{errors.rating.message}</p>
        )}
      </div>

      <div className="flex flex-col sm:flex-row gap-2 mt-8">
        <Button
          size="icon"
          variant="destructive"
          onClick={handleRemove}
          disabled={isLoading}
          className="w-full sm:w-auto"
        >
          <Trash2/>
        </Button>
        <div className="flex grow gap-2 w-full sm:w-auto">
          <Button
            type="button"
            variant="outline"
            onClick={onCancel}
            disabled={isLoading}
            className="flex-1"
          >
            Cancel
          </Button>
          <Button
            type="submit"
            disabled={isLoading}
            className="flex-1"
          >
            {isLoading ? "Updating..." : "Update"}
          </Button>
        </div>
      </div>
    </form>
  );
} 