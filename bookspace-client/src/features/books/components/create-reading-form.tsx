import { useForm } from "react-hook-form";
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
import { addReadingListItem } from "@/services/reading-list.service";
import { ReadingListDTOStatusEnum, getReadingListStatusLabel } from "@/types/reading-list-dto";
import type { ReadingListRequestDTO } from "@/types/reading-list-request-dto";

interface CreateReadingFormProps {
  bookId: number;
  bookTitle: string;
  username: string;
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

export function CreateReadingForm({
  bookId,
  username,
  onSuccess,
  onCancel,
}: CreateReadingFormProps) {
  const queryClient = useQueryClient();

  const {
    handleSubmit,
    setValue,
    watch,
    formState: { errors, isSubmitting },
  } = useForm<FormData>({
    defaultValues: {
      status: ReadingListDTOStatusEnum.PLANTOREAD,
      rating: 0,
    },
  });

  const watchedStatus = watch("status");
  const watchedRating = watch("rating");

  const addMutation = useMutation({
    mutationFn: (data: ReadingListRequestDTO) =>
      addReadingListItem(username, data),
    onSuccess: () => {
      toast.success("Book added to reading list!");
      queryClient.invalidateQueries({ queryKey: ["userReadingItem"] });
      onSuccess();
    },
    onError: () => {
      toast.error("Failed to add book to reading list");
    },
  });

  const onSubmit = (data: FormData) => {
    const requestData: ReadingListRequestDTO = {
      bookID: bookId,
      status: data.status,
      rating: data.rating > 0 ? data.rating : undefined,
    };

    addMutation.mutate(requestData);
  };

  const isLoading = isSubmitting || addMutation.isPending;

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="space-y-2">
        <label className="text-sm font-medium">Initial Reading Status</label>
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
        <label className="text-sm font-medium">Initial Rating (Optional)</label>
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

      <div className="flex grow gap-2 mt-8">
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
          {isLoading ? "Adding..." : "Add to List"}
        </Button>
      </div>
    </form>
  );
} 