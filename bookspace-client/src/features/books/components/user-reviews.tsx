import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar.tsx"
import {Card, CardContent} from "@/components/ui/card.tsx"
import {Rating, RatingButton} from "@/components/ui/rating.tsx";
import type {ReviewDTO} from "@/types/review-dto.ts";

type Props = {
    review: ReviewDTO
};
const UserReviews = (props: Props) => {

    // Generate initials from username
    const getInitials = (name: string) => {
        return name
            .split(" ")
            .map((word) => word.charAt(0))
            .join("")
            .toUpperCase()
            .slice(0, 2)
    }

    // Format date
    const formatDate = (date?: Date) => {
        if (!date) return ""

        const now = new Date()
        const reviewDate = new Date(date)
        const diffInDays = Math.floor((now.getTime() - reviewDate.getTime()) / (1000 * 60 * 60 * 24))

        if (diffInDays === 0) return "Today"
        if (diffInDays === 1) return "Yesterday"
        if (diffInDays < 7) return `${diffInDays} days ago`
        if (diffInDays < 30) return `${Math.floor(diffInDays / 7)} weeks ago`
        if (diffInDays < 365) return `${Math.floor(diffInDays / 30)} months ago`

        return reviewDate.toLocaleDateString()
    }


    return (
        <Card className="w-full md:w-3xl">
            <CardContent className="p-4">
                <div className="flex items-start space-x-3">
                    <Avatar className="w-12 h-12">
                        <AvatarImage src={`https://api.dicebear.com/9.x/lorelei-neutral/svg?seed=${props.review.username}`} alt={props.review.username}/>
                        <AvatarFallback className="bg-gray-100 text-gray-600 text-sm font-medium">
                            {getInitials(props.review.username as string)}
                        </AvatarFallback>
                    </Avatar>

                    <div className="flex-1 space-y-2">
                        <div className="flex items-center justify-between">
                            <div className="flex flex-col items-start space-y-2">
                                <h4 className="font-semibold text-sm">{props.review.username}</h4>
                                <div className="flex items-center space-x-1">
                                    <Rating defaultValue={props.review.rating} readOnly className="text-primary">
                                        {Array.from({length: 5}).map((_, index) => (
                                            <RatingButton key={index}/>
                                        ))}
                                    </Rating>
                                </div>
                            </div>
                            {props.review.createdAt && <span className="text-xs text-muted-foreground">{formatDate(props.review.createdAt)}</span>}
                        </div>

                        <p className="text-sm text-muted-foreground leading-relaxed">{props.review.comment}</p>
                    </div>
                </div>
            </CardContent>
        </Card>
    )
};

export default UserReviews;