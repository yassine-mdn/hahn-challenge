import ProfileCard from "../components/profile-card";
import {ReadingList} from "../components/reading-list";
import ReviewList from "@/features/users/components/review-list.tsx";
import {useParams} from "react-router";
import {useQuery} from "@tanstack/react-query";
import {fetchUserReviews} from "@/services/review.service.ts";
import {fetchUserReadingList} from "@/services/reading-list.service.ts";
import {fetchUserByUsername} from "@/services/user.service.ts";


const UserDetails = () => {

    let params = useParams();

    const reviews = useQuery({
        queryKey: ["reviews","user", params.username],
        queryFn: () => fetchUserReviews(params.username as string)
    })

    const readingList = useQuery({
        queryKey: ["reading-list", params.username],
        queryFn: () => fetchUserReadingList(params.username as string,0,8),
    });

    const user = useQuery({
        queryKey: ["user", params.username],
        queryFn: () => fetchUserByUsername(params.username as string)
    })



    return (

        <div className="max-w-6xl mx-auto px-4 py-8">
            {user.isSuccess && <ProfileCard user={user.data}/>}
            <div className="mt-8">
                {readingList.isSuccess && <ReadingList readingList={readingList.data.content}/>}

                {reviews.isSuccess && <ReviewList reviews={reviews.data.content}/>}
            </div>

        </div>

    );
};

export default UserDetails; 