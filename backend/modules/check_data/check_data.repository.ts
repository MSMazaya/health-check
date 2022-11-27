import { ObjectId } from "https://deno.land/x/web_bson@v0.2.5/mod.ts";
import db from "../../utils/db.ts";
import { CheckDataModel, CheckDataSchema } from "./check_data.model.ts";

const createCheckData = async (checkData: CheckDataModel) => {
  const checkDataCollection = db.collection<CheckDataSchema>("check_data");

  await checkDataCollection.insertOne(checkData);
};

const getCheckDatas = async (userId: ObjectId) => {
  const checkDataCollection = db.collection<CheckDataSchema>("check_data");

  const checkData = await checkDataCollection.find({
    userId,
  }).map((checkData) => checkData);

  return checkData;
};

export default {
  getCheckDatas,
  createCheckData,
};
