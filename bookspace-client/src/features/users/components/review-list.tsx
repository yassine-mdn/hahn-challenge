import type {ReviewDTO} from "@/types/review-dto.ts";
import UserReview from "@/features/users/components/user-review.tsx";

type Props = {
    reviews: ReviewDTO[]
};
const ReviewList = (props: Props) => {
    return (
        <div>
            <div className="flex flex-col  justify-start items-start mb-4">
                <span className="text-xl font-semibold">Reviews</span>
                <span className={"text-ld text-muted-foreground"}>Recently reviewed</span>
            </div>
            <div className="flex flex-col  justify-start items-start gap-4">
                {props.reviews.map((review: ReviewDTO, index: number) => (
                    <UserReview key={index} review={review}/>
                ))}
            </div>

        </div>
    );
};

export default ReviewList;