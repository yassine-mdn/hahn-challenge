import {Input} from "@/components/ui/input";
import {Separator} from "@/components/ui/separator.tsx";
import {Button} from "@/components/ui/button";
import {Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";
import {useForm} from "react-hook-form";
import {useMutation} from "@tanstack/react-query";
import {toast} from "sonner";
import {useAuth} from "@/features/auth/AuthContext";
import {updateUsername} from "@/services/user.service";
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod";

const updateUsernameSchema = z.object({
    username: z.string().min(3, "Username must be at least 3 characters").max(20, "Username must be less than 20 characters"),
});

type UpdateUsernameForm = z.infer<typeof updateUsernameSchema>;

const UserSettings = () => {
    const {user, updateUser} = useAuth();

    const form = useForm<UpdateUsernameForm>({
        resolver: zodResolver(updateUsernameSchema),
        defaultValues: {
            username: user || "",
        },
    });

    const updateUsernameMutation = useMutation({
        mutationFn: ({currentName, newName}: { currentName: string; newName: string }) =>
            updateUsername(currentName, newName),
        onSuccess: (data) => {
            toast.success("Username updated successfully!");
            updateUser(data.username as string);
        },
        onError: (error: any) => {
            if (error.response?.status === 409) {
                toast.error("Username is already in use. Please choose a different one.");
            } else {
                toast.error("Failed to update username. Please try again.");
            }
        },
    });

    const onSubmit = (data: UpdateUsernameForm) => {
        if (user) {
            updateUsernameMutation.mutate({currentName: user, newName: data.username});
        }
    };

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">User Settings</h1>
            <p>Manage your account settings</p>
            <Separator className="my-4"/>
            <div className={"flex justify-center"}>
                <div className="w-full max-w-2xl">
                    <h1 className="text-2xl font-bold mb-4">Profile</h1>
                    <p>This is how others will see you on the site</p>
                    <Separator className="my-4"/>

                    <h2 className={"mb-4 font-semibold"}>Update Username</h2>

                    <Form {...form}>
                        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                            <FormField
                                control={form.control}
                                name="username"
                                render={({field}) => (
                                    <FormItem>
                                        <FormLabel>Username</FormLabel>
                                        <FormControl>
                                            <Input
                                                placeholder="Enter new username"
                                                {...field}
                                            />
                                        </FormControl>
                                        <FormMessage/>
                                    </FormItem>
                                )}
                            />
                            <FormDescription>
                                This is your public display name. It can be your real name or a
                                pseudonym. You can only change this once every 30 days (not really) and must be unique.
                            </FormDescription>
                            <Button
                                type="submit"
                                disabled={updateUsernameMutation.isPending}
                                className="w-full"
                            >
                                {updateUsernameMutation.isPending ? "Updating..." : "Update Username"}
                            </Button>
                        </form>
                    </Form>

                </div>
            </div>
        </div>
    );
};

export default UserSettings; 