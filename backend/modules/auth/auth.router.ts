import { Router } from "https://deno.land/x/oak@v11.1.0/router.ts";
import authController from "./auth.controller.ts";

const router = new Router();

router.post("/login", authController.login);

export default router.routes();
