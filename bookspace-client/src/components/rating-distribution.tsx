import {Rating, RatingButton} from "@/components/ui/rating";
import {Progress} from "@/components/ui/progress";
import type {RatingCountDTO} from "@/types/rating-count-dto.ts";

type Props = {
    ratings: RatingCountDTO[]
};
const RatingDistribution = (props: Props) => {
    const totalRatings = props.ratings.reduce((sum, r) => sum + (r.count ?? 0), 0);

    const averageRating = (() => {
        const totalScore = props.ratings.reduce((sum, r) => sum + (r.rating ?? 0) * (r.count ?? 0), 0);
        return totalRatings === 0 ? 0 : totalScore / totalRatings;
    })();

    const ratingsMap = new Map<number, number>();
    props.ratings.forEach(r => {
        if (r.rating != null && r.count != null) {
            ratingsMap.set(r.rating, r.count);
        }
    });

    return (
        <div className="space-y-3">
            <div className="flex items-center gap-2 text-2xl font-semibold">
                <Rating defaultValue={averageRating} readOnly className="text-primary">
                    {Array.from({length: 5}).map((_, index) => (
                        <RatingButton key={index}/>
                    ))}
                </Rating>
                <span>{averageRating.toFixed(2)}</span>
                <span className="text-muted-foreground text-sm">
                    {totalRatings.toLocaleString()} ratings
                </span>
            </div>

            {[5, 4, 3, 2, 1].map(star => {
                const count = ratingsMap.get(star) ?? 0;
                const percent = totalRatings ? (count / totalRatings) * 100 : 0;

                return (
                    <div key={star} className="flex items-center gap-4">
                        <span className="w-12 text-nowrap text-sm">{star} stars</span>
                        <Progress className="max-w-xl h-2" value={percent}/>
                        <span className="w-20 text-xs text-nowrap text-right text-muted-foreground">
              {count.toLocaleString()} ({Math.round(percent)}%)
            </span>
                    </div>
                );
            })}
        </div>
    )
};

export default RatingDistribution;