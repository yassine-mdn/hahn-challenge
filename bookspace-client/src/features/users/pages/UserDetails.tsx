import ProfileCard from "../components/profile-card";
import {ReadingList} from "../components/reading-list";
import {type UserDTO} from "@/types/user-dto";
import {type ReadingListDTO, ReadingListDTOStatusEnum} from "@/types/reading-list-dto";
import ReviewList from "@/features/users/components/review-list.tsx";
import type {ReviewDTO} from "@/types/review-dto.ts";

const user: UserDTO = {
    id: "1",
    username: "John Doe",
    email: "john.doe@example.com",
    createdAt: new Date(),
}



 const sampleReadingList: ReadingListDTO[] = [
  {
    id: 1,
    username: "alice",
    book: {
      id: 101,
      title: "The Enchanted Forest",
      coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg"
    },
    addedAt: new Date("2024-01-10"),
    startedAt: new Date("2024-01-12"),
    completedAt: new Date("2024-01-20"),
    status: ReadingListDTOStatusEnum.COMPLETED,
    rating: 5
  },
  {
    id: 2,
    username: "bob",
    book: {
      id: 102,
      title: "Shadows of the Past",
      coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg"
    },
    addedAt: new Date("2024-02-01"),
    startedAt: new Date("2024-02-05"),
    status: ReadingListDTOStatusEnum.READING,
    rating: 4
  },
  {
    id: 3,
    username: "carol",
    book: {
      id: 103,
      title: "Beyond the Stars",
      coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg"
    },
    addedAt: new Date("2024-03-15"),
    status: ReadingListDTOStatusEnum.PLANTOREAD
  },
  {
    id: 4,
    username: "dave",
    book: {
      id: 104,
      title: "History of Time",
      coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg"
    },
    addedAt: new Date("2024-04-01"),
    startedAt: new Date("2024-04-02"),
    status: ReadingListDTOStatusEnum.ONHOLD,
    rating: 3
  },
  {
    id: 5,
    username: "eve",
    book: {
      id: 105,
      title: "Philosophy 101",
      coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg"
    },
    addedAt: new Date("2024-05-01"),
    status: ReadingListDTOStatusEnum.DROPPED
  }
];

const sampleReviews: ReviewDTO[] = [
    {
        id: 1,
        username: "Sarah Johnson",
        comment:
            "Absolutely love this product! The quality is outstanding and it exceeded my expectations. The customer service was also fantastic. Highly recommend to anyone looking for a reliable solution.",
        rating: 5,
        createdAt: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000), // 2 days ago
        book: {
            id: 105,
            title: "Philosophy 101",
            coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg"
        }
    },
    {
        id: 2,
        username: "Mike Chen",
        comment:
            "Good value for money. Works as expected, though the setup could be a bit more intuitive. Overall satisfied with the purchase.",
        rating: 4,
        createdAt: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000), // 1 week ago
        book: {
            id: 105,
            title: "Philosophy 101",
            coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg"
        }
    },
    {
        id: 3,
        username: "Emily Rodriguez",
        comment: "Perfect! Exactly what I was looking for. Fast shipping and great packaging too.",
        rating: 5,
        createdAt: new Date(Date.now() - 1 * 24 * 60 * 60 * 1000), // 1 day ago
        book: {
            id: 105,
            title: "Philosophy 101",
            coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg"
        }
    },
]

const UserDetails = () => {
    return (
    
        <div className="max-w-6xl mx-auto px-4 py-8">
          <ProfileCard user={user} />
          <div className="mt-8">
            <ReadingList readingList={sampleReadingList} />
              <ReviewList reviews={sampleReviews}/>
          </div>
          
        </div>
      
    );
};

export default UserDetails; 