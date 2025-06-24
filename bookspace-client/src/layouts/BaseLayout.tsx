import {Navbar} from "@/layouts/components/navbar.tsx";
import {Outlet} from "react-router";
import Footer from "@/layouts/components/footer.tsx";

const BaseLayout = () => {
    return (
        <div className={"pt-16 min-h-screen overflow-hidden flex flex-col"}>
            <Navbar/>
            <div className={"w-full container mx-auto py-8 mb-8 px-2 lg:px-0 overflow-x-hidden"}>
                <Outlet/>
            </div>
            <Footer/>
        </div>
    );
};

export default BaseLayout;