import {cn} from "@/lib/utils"
import {Button} from "@/components/ui/button"
import {Card, CardContent, CardHeader, CardTitle,} from "@/components/ui/card"
import {Input} from "@/components/ui/input"
import {Label} from "@/components/ui/label"
import Logo from "@/components/ui/logo.tsx";
import {Link} from "react-router";
import {useForm} from "react-hook-form"
import {useMutation} from "@tanstack/react-query"
import {useAuth} from "@/features/auth/AuthContext"
import type {AuthenticationRequestDTO} from "@/types/authentication-request-dto"
import {useState} from "react"

export function LoginForm({
                              className,
                              ...props
                          }: React.ComponentProps<"div">) {
    const { login } = useAuth();
    const [error, setError] = useState<string | null>(null);
    const { register, handleSubmit, formState: { errors } } = useForm<AuthenticationRequestDTO>();

    const mutation = useMutation<void, Error, AuthenticationRequestDTO>({
        mutationFn: async (data) => {
           login(data)
        }
    });

    const onSubmit = (data: AuthenticationRequestDTO) => {
        setError(null);
        mutation.mutate(data);
    };

    return (
        <div className={cn("flex flex-col gap-6", className)} {...props}>
            <Card>
                <CardHeader className="flex flex-col justify-center">
                    <a href="#" className="">

                    </a>
                    <CardTitle className="text-xl text-center flex items-center gap-2 self-center font-medium">
                            <Logo className="size-8" />
                        BookSpace
                    </CardTitle>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleSubmit(onSubmit)}>
                        <div className="grid gap-6">
                            <div className="grid gap-6">
                                <div className="grid gap-3">
                                    <Label htmlFor="username">Username</Label>
                                    <Input
                                        id="username"
                                        type="text"
                                        placeholder="your username"
                                        autoComplete="username"
                                        {...register("username", { required: "Username is required" })}
                                        aria-invalid={!!errors.username}
                                    />
                                    {errors.username && (
                                        <span className="text-xs text-red-500">{errors.username.message}</span>
                                    )}
                                </div>
                                <div className="grid gap-3">
                                    <div className="flex items-center">
                                        <Label htmlFor="password">Password</Label>
                                        <a
                                            href="#"
                                            className="ml-auto text-sm underline-offset-4 hover:underline"
                                        >
                                            Forgot your password?
                                        </a>
                                    </div>
                                    <Input
                                        id="password"
                                        type="password"
                                        autoComplete="current-password"
                                        {...register("password", { required: "Password is required" })}
                                        aria-invalid={!!errors.password}
                                    />
                                    {errors.password && (
                                        <span className="text-xs text-red-500">{errors.password.message}</span>
                                    )}
                                </div>
                                {error && <div className="text-xs text-red-500 text-center">{error}</div>}
                                <Button type="submit" className="w-full" disabled={mutation.isPending}>
                                    {mutation.isPending ? "Logging in..." : "Login"}
                                </Button>
                            </div>
                            <div className="text-center text-sm">
                                Don&apos;t have an account?{" "}
                                <Link to={"/sign-up"} className="underline underline-offset-4">
                                    Sign up
                                </Link>
                            </div>
                        </div>
                    </form>
                </CardContent>
            </Card>
            <div
                className="text-muted-foreground *:[a]:hover:text-primary text-center text-xs text-balance *:[a]:underline *:[a]:underline-offset-4">
                By clicking continue, you agree to our <a href="#">Terms of Service</a>{" "}
                and <a href="#">Privacy Policy</a>.
            </div>
        </div>
    )
}
