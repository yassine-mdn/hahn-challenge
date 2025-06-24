import ProfileCard from "../components/profile-card";
import {ReadingList} from "../components/reading-list";
import {type UserDTO} from "@/types/user-dto";
import {type ReadingListDTO, ReadingListDTOStatusEnum} from "@/types/reading-list-dto";

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

const UserDetails = () => {
    return (
    
        <div className="max-w-6xl mx-auto px-4 py-8">
          <ProfileCard user={user} />
          <div className="mt-8">
            <ReadingList readingList={sampleReadingList} />
          </div>
          
        </div>
      
    );
};

export default UserDetails; 