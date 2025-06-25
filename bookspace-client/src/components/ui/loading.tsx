import Logo from "@/components/ui/logo.tsx";

const Loading = () => {
    return (
        <div className="h-screen w-full grid place-content-center">
            <div className="animate-pulse fade-in-0 duration-1000">
                <Logo className="w-12 h-12"/>
            </div>
        </div>
    );
};

export default Loading;