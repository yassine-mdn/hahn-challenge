import { useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createReview } from "@/services/review.service";
import type { ReviewRequestDTO } from "@/types/review-request-dto";
import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import type { ReadingListDTO } from "@/types/reading-list-dto";
import { toast } from "sonner";

interface AddReviewCardProps {
  readingList?: ReadingListDTO;
}

const AddReviewCard = ({ readingList }: AddReviewCardProps) => {
  const [comment, setComment] = useState("");
  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: async (payload: ReviewRequestDTO) => createReview(payload),
    onSuccess: () => {
      setComment("");
      toast.success("Review submitted!");
      queryClient.invalidateQueries({ queryKey: ["userReadingItem"] });
      queryClient.invalidateQueries({ queryKey: ["reviews"] });
    },
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    mutation.mutate({
      readingListId: readingList!.id,
      comment,
    });
  };

  if (!readingList) {
    return null;
  }

  return (
    <Card className="w-full md:w-3xl gap-2">
      <CardHeader>
        <CardTitle>Add a Review</CardTitle>
      </CardHeader>
      <CardContent className="px-4 py-0">
        <form onSubmit={handleSubmit} className="flex flex-col gap-2">
          <textarea
            className="border rounded p-2 min-h-[80px]"
            placeholder="Write your review..."
            value={comment}
            onChange={e => setComment(e.target.value)}
            disabled={mutation.isPending}
            required
          />
          <Button type="submit" disabled={mutation.isPending || !comment.trim()}>
            {mutation.isPending ? "Submitting..." : "Submit Review"}
          </Button>
        </form>
      </CardContent>
    </Card>
  );
};

export default AddReviewCard; 