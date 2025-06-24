import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { type UserDTO } from "@/types/user-dto";
import {useAuth} from "@/features/auth/AuthContext.tsx";

type ProfileCardProps = {
    user: UserDTO
}

const ProfileCard = (props: ProfileCardProps) => {

  const {user} = useAuth();

    const getInitials = (name: string) => {
        return name
            .split(" ")
            .map((word) => word.charAt(0))
            .join("")
            .toUpperCase()
            .slice(0, 2)
    }

  return (
    <Card >
    <CardContent className="">
      <div className="flex flex-col md:flex-row items-start md:items-center space-y-4 md:space-y-0 md:space-x-6">
        <Avatar className="w-24 h-24">
          <AvatarImage src={`https://api.dicebear.com/9.x/lorelei-neutral/svg?seed=${props.user.username}`} alt="Sophia Bennett" />
          <AvatarFallback>{getInitials(props.user.username as string)}</AvatarFallback>
        </Avatar>
        
        <div className="flex-1">
          <h1 className="text-2xl font-bold  mb-1">{props.user.username}</h1>
          <p className="text-muted-foreground mb-2">Avid reader and book reviewer</p>
          <p className="text-sm text-muted-foreground">Joined in {props.user.createdAt?.getFullYear()}</p>
        </div>

        {user == props.user.username && (
            <Button variant="outline" className="shrink-0">
              Edit Profile
            </Button>
        )}
      </div>
    </CardContent>
  </Card>
  )
}

export default ProfileCard