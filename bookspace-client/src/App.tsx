import './App.css'
import {Route, Routes} from "react-router";
import BaseLayout from "@/layouts/BaseLayout.tsx";
import Home from "@/routes/Home.tsx";

function App() {

    return (
        <Routes>
            <Route element={<BaseLayout/>}>
                <Route index element={<Home />} />
                <Route path={"my-books"} element={<span>hhhhh</span>}/>
            </Route>
        </Routes>
    )
}

export default App
