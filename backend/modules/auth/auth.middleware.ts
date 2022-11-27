import { Context } from "https://deno.land/x/oak@v11.1.0/context.ts";
import { authenticate } from "../../utils/jwt.ts";

const authentication = async (
  context: Context<Record<string, any>, Record<string, any>>,
  next: () => Promise<unknown>,
) => {
  const headers = context.request.headers;
  const authorization = headers.get("Authorization");
  const token = authorization?.split(" ")[1];

  if (!token) {
    context.response.body = {
      status: 401,
      message: "Unauthorized",
    };
    return;
  }

  const [isValid, payload] = await authenticate(token);

  if (isValid) {
    context.state.user = payload;
  } else {
    context.response.body = {
      status: 401,
      message: "Unauthorized",
    };

    return;
  }

  await next();
};

export default {
  authentication,
};
