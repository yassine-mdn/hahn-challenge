import {LogOut, Menu, Search, Settings2} from "lucide-react";
import {Button} from "@/components/ui/button";
import {Sheet, SheetContent, SheetTrigger,} from "@/components/ui/sheet";
import {Link, useNavigate, useSearchParams} from "react-router";
import {useState} from "react";
import {Input} from "@/components/ui/input.tsx";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import {useAuth} from "@/features/auth/AuthContext";
import {useMutation} from "@tanstack/react-query";
import {toast} from "sonner";
import {useIsMobile} from "@/hooks/use-mobile";

interface MenuItem {
    name: string;
    link: string;
    icon?: React.ReactNode;
}

const Navbar = () => {

    const [isOpen, setIsOpen] = useState(false)
    const { user, logout, isAuthenticated } = useAuth();
    const isMobile = useIsMobile();
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const [searchValue, setSearchValue] = useState(searchParams.get('q') || '');
    
    const logoutMutation = useMutation({
        mutationFn: async () => {
            logout();
        },
        onSuccess: () => {
            toast("Logged out successfully.");
        },
    });

    const handleSearch = (e: React.FormEvent) => {
        e.preventDefault();
        if (searchValue.trim()) {
            navigate(`/browse?q=${encodeURIComponent(searchValue.trim())}`);
        }
    };

    const navigationItems: MenuItem[] = [
        { name: "Home", link: "/" },
        { name: "Browse", link: "/browse" },
    ]

    return (
        <header className="fixed top-0 z-50 w-full border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
            <div className=" flex h-16 items-center justify-between px-4">
                <div className="flex items-center gap-8 justify-between">
                    <Link to="/" className="flex items-center space-x-2">
                        <svg
                            xmlns="http://www.w3.org/2000/svg"
                            viewBox="0 0 16 16"
                            className="w-6 h-6"
                            fill="none"
                        >
                            <g clipPath="url(#a)" >
                                <path
                                    fill="white"
                                    fillRule="evenodd"
                                    d="M8 1.333H2v8.889h6v4.445h6V5.778H8z"
                                    clipRule="evenodd"
                                />
                            </g>
                            <defs>
                                <clipPath id="a">
                                    <path fill="white" d="M0 0h16v16H0z" />
                                </clipPath>
                            </defs>
                        </svg>
                        <span className={"text-2xl font-bold hidden md:block"}>BookSpace</span>
                    </Link>

                    <nav className="hidden md:flex items-center space-x-6">
                        {navigationItems.map((item) => (
                            <Link key={item.name} to={item.link} className="text-sm font-medium transition-colors hover:text-primary">
                                {item.name}
                            </Link>
                        ))}
                    </nav>
                </div>

                <div className="flex items-center justify-center gap-6">
                    <form onSubmit={handleSearch} className="relative">
                        <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
                        <Input 
                            type="search" 
                            placeholder="Search books..." 
                            className="pl-8 w-[300px] lg:w-[400px] border-0"
                            value={searchValue}
                            onChange={(e) => setSearchValue(e.target.value)}
                        />
                    </form>
                    {isAuthenticated ? (
                        <DropdownMenu>
                            <DropdownMenuTrigger asChild>
                                <Button size={"icon"} variant="ghost" className="hidden md:flex items-center gap-2 px-3">
                                    <Avatar className="h-8 w-8 rounded-lg">
                                        <AvatarImage src={`https://api.dicebear.com/9.x/lorelei-neutral/svg?seed=${user}`} alt={user || "User"} />
                                        <AvatarFallback className="rounded-lg">{user?.[0]?.toUpperCase() || "U"}</AvatarFallback>
                                    </Avatar>
                                </Button>
                            </DropdownMenuTrigger>
                            <DropdownMenuContent side={isMobile ? "bottom" : "right"} align="end" sideOffset={4} className="min-w-56 rounded-lg">
                                <DropdownMenuLabel className="p-0 font-normal">
                                    <Link to={`/user/${user}`} className="flex items-center gap-2 px-1 py-1.5 text-left text-sm">

                                        <Avatar className="h-8 w-8 rounded-lg">
                                            <AvatarImage src={`https://api.dicebear.com/9.x/lorelei-neutral/svg?seed=${user}`} alt={user || "User"} />
                                            <AvatarFallback className="rounded-lg">{user?.[0]?.toUpperCase() || "U"}</AvatarFallback>
                                        </Avatar>
                                        <div className="grid flex-1 text-left text-sm leading-tight">
                                            <span className="truncate font-medium">{user || "User"}</span>
                                        </div>
                                    </Link>
                                </DropdownMenuLabel>
                                <DropdownMenuSeparator />
                                <DropdownMenuItem asChild>
                                    <Link to={`/user/${user}/settings`} className={"flex space-x-2"}>
                                        <Settings2/>
                                        Settings
                                    </Link>
                                </DropdownMenuItem>
                                <DropdownMenuSeparator />
                                <DropdownMenuItem onClick={() => logoutMutation.mutate()}>
                                    <LogOut className="mr-2" /> Log out
                                </DropdownMenuItem>
                            </DropdownMenuContent>
                        </DropdownMenu>
                    ) : (
                        <Button className={"hidden md:block"} asChild>
                            <Link to="/login">Login</Link>
                        </Button>
                    )}
                </div>

                <Sheet open={isOpen} onOpenChange={setIsOpen}>
                    <SheetTrigger asChild>
                        <Button variant="ghost" size="icon" className="md:hidden">
                            <Menu className="!h-8 !w-8"/>
                            <span className="sr-only">Toggle menu</span>
                        </Button>
                    </SheetTrigger>
                    <SheetContent side="right" className="w-[300px] sm:w-[400px]">
                        <div className="flex flex-col space-y-4 my-12 px-6 h-full">
                            <nav className="flex flex-col space-y-2">
                                {navigationItems.map((item) => (
                                    <Link
                                        key={item.name}
                                        to={item.link}
                                        className="block px-2 py-1 text-lg font-medium transition-colors hover:text-primary"
                                        onClick={() => setIsOpen(false)}
                                    >
                                        {item.name}
                                    </Link>
                                ))}
                            </nav>

                            <Button className="mt-auto" asChild>
                                <Link to="/login" onClick={() => setIsOpen(false)}>
                                    Login
                                </Link>
                            </Button>
                        </div>
                    </SheetContent>
                </Sheet>
            </div>
        </header>
    );
};


export {Navbar};
