import {Navbar} from "@/layouts/components/navbar.tsx";

const BaseLayout = () => {
    return (
        <div className={"pt-16 min-h-screen min-w-screen overflow-hidden"}>
            <Navbar/>
        </div>
    );
};

export default BaseLayout;