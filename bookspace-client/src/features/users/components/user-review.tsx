import type {ReviewDTO} from "@/types/review-dto.ts";
import {Rating, RatingButton} from "@/components/ui/rating.tsx";
import {format} from "date-fns";
import {Card, CardContent} from "@/components/ui/card.tsx";
import { Link } from "react-router";

type Props = {
    review: ReviewDTO
};
const UserReview = (props: Props) => {
    return (
        <Card className={"!p-0 w-full"}>
            <CardContent className={"flex gap-2 p-4"}>
                <Link to={`/book/${props.review.book?.id}`}>
                <img src={props.review.book?.coverUrl} alt={"cover"} className={" max-w-32 object-cover rounded-lg !aspect-5/8"}/>
                </Link>
                <div className={"flex flex-col gap-2"}>
                    <div className={"flex flex-col space-y-1"}>
                        <Rating defaultValue={props.review.rating} readOnly>
                            {Array.from({length: 5}).map((_, index) => (
                                <RatingButton key={index}/>
                            ))}
                        </Rating>
                        <span className={"text-sm font-light text-muted-foreground"}>{format(props.review.createdAt as Date, "dd-MM-yyyy")}</span>
                        <p>{props.review.comment}</p>
                    </div>
                </div>
            </CardContent>
        </Card>
    );
};

export default UserReview;