import { ObjectId } from "https://deno.land/x/web_bson@v0.2.5/mod.ts";
import db from "../../utils/db.ts";
import {
  CheckDataModel,
  CheckDataSchema,
  GetCheckDataFilter,
} from "./check_data.model.ts";

export const createCheckData = async (checkData: CheckDataModel) => {
  const checkDataCollection = db.collection<CheckDataSchema>("check_data");

  await checkDataCollection.insertOne(checkData);
};

const getCheckDatas = async (userId: ObjectId, filter: GetCheckDataFilter) => {
  const checkDataCollection = db.collection<CheckDataSchema>("check_data");

  const checkData = await checkDataCollection.find({
    userId,
  }, {
    limit: filter.limit,
  }).map((checkData) => checkData);

  if (filter.dateStart && filter.dateEnd) {
    return checkData.filter((data) => {
      const startAt = new Date(filter.dateStart!);
      const endAt = new Date(filter.dateEnd!);

      const dataDate = new Date(data.date);

      return dataDate > startAt && dataDate < endAt;
    });
  }

  return checkData;
};

export default {
  getCheckDatas,
  createCheckData,
};
