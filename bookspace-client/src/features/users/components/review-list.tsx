import type {ReviewDTO} from "@/types/review-dto.ts";
import UserReview from "@/features/users/components/user-review.tsx";
import {Card, CardContent} from "@/components/ui/card";

type Props = {
    reviews: ReviewDTO[]
};
const ReviewList = ({reviews}: Props) => {

    const hasReviews = reviews && reviews.length > 0

    return (
        <div>
            <div className="flex flex-col  justify-start items-start mb-4">
                <span className="text-xl font-semibold">Reviews</span>
                <span className={"text-ld text-muted-foreground"}>Recently reviewed</span>
            </div>
            {hasReviews ? (
                <div className="flex flex-col justify-start items-start gap-4">
                    {reviews.map((review) => (
                        <UserReview key={review.id} review={review} />
                    ))}
                </div>
            ) : (
                <Card className="w-full">
                    <CardContent className="h-40 grid place-items-center">No reviews yet</CardContent>
                </Card>
            )}

        </div>
    );
};

export default ReviewList;