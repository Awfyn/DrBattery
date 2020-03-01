package com.awfyn.drbattery.oracle;

import android.os.AsyncTask;
import android.os.BatteryManager;

import com.awfyn.drbattery.database.dao_s.OracleDao;
import com.awfyn.drbattery.database.entities.Mineral;
import com.awfyn.drbattery.database.entities.Vision;

import java.util.Collections;
import java.util.List;

public class Oracle {
    private static final String TAG = "TAG_ORACLE";

    public Oracle() {}

    public void getRemainingTime(FalsePromise falsePromise, OracleDao dao, float batteryLevel) {
        new AnticipateRemainingTime(falsePromise, dao, batteryLevel).execute();
    }

    public void getBestRemainingTime(FalsePromise falsePromise, OracleDao dao, float batteryLevel) {
        new BestRemainingTime(falsePromise, dao, batteryLevel).execute();
    }

    public void getWorstRemainingTime(FalsePromise falsePromise, OracleDao dao, float batteryLevel) {
        new WorstRemainingTime(falsePromise, dao, batteryLevel).execute();
    }


    private static class BestRemainingTime extends AsyncTask<Void, Void, Void> {
        private float currentBatteryLevel;
        private FalsePromise falsePromise;
        private OracleDao dao;

        private BestRemainingTime(FalsePromise falsePromise, OracleDao dao, float batteryLevel) {
            currentBatteryLevel = batteryLevel;
            this.falsePromise = falsePromise;
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                falsePromise.healed(dao.getBestVision() * currentBatteryLevel);
            } catch (Exception e) {
                falsePromise.died();
            }
            return null;
        }
    }

    private static class WorstRemainingTime extends AsyncTask<Void, Void, Void> {
        private float currentBatteryLevel;
        private FalsePromise falsePromise;
        private OracleDao dao;

        private WorstRemainingTime(FalsePromise falsePromise, OracleDao dao, float batteryLevel) {
            currentBatteryLevel = batteryLevel;
            this.falsePromise = falsePromise;
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                falsePromise.healed(dao.getWorstVision() * currentBatteryLevel);
            } catch (Exception e) {
                falsePromise.died();
            }
            return null;
        }
    }

    private static class AnticipateRemainingTime extends AsyncTask<Void, Void, Void> {
        private float currentBatteryLevel;
        private FalsePromise falsePromise;
        private OracleDao dao;

        private AnticipateRemainingTime(FalsePromise falsePromise, OracleDao dao, float batteryLevel) {
            currentBatteryLevel = batteryLevel;
            this.falsePromise = falsePromise;
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void[] voids) {
            List<Mineral> minerals = dao.getNotCalculatedMinerals();
            if (minerals.size() < 50) {
                falsePromise.died();
                return null;
            }

            int i = 0;
            Mineral preMineral = minerals.get(i);
            Mineral postMineral = minerals.get(++i);
            Vision vision = new Vision();
            for (i = 2; i < minerals.size(); i++) {
                if (i > 2) {
                    preMineral = postMineral;
                    postMineral = minerals.get(i);
                }
                if (preMineral.getStatus() !=3 || postMineral.getStatus()!=3)
                    continue;
//                if (preMineral.getStatus() != postMineral.getStatus()) {
//                    preMineral.setCalculated(true);
//                    preMineral = postMineral;
//                    postMineral = minerals.get(i);
//                    continue;
//                }
                if (preMineral.getLevel() == postMineral.getLevel()) {
                    postMineral.setCalculated(true);
                    postMineral = minerals.get(i);
                    continue;
                }
                vision.setStatus(BatteryManager.BATTERY_STATUS_DISCHARGING);
                vision.setBatteryLevel((int) currentBatteryLevel);

                vision.setBatteryLifeMsPerLevel(
                        (Math.abs(postMineral.getTimeStamp() - preMineral.getTimeStamp()))
                        / Math.abs(postMineral.getLevel() - preMineral.getLevel()));

                dao.sketchVision(vision);
            }

            Collections.reverse(minerals);
            Mineral lastMineral = null;

            for (int j = 0; j < minerals.size(); j++) {
                lastMineral = minerals.get(j);
                if (currentBatteryLevel - lastMineral.getLevel() >= 5) {
                    break;
                }
            }

            if (lastMineral == null) {
                falsePromise.died();
                return null;
            }

            falsePromise.healed(
                    (Math.abs(System.currentTimeMillis() - lastMineral.getTimeStamp()))
                            / Math.abs(currentBatteryLevel - lastMineral.getLevel()));

            return null;
            //******************************************************************************************


//            List<Mineral> minerals = dao.getNotCalculatedMinerals();
//
//            for (int i = 1; i < minerals.size(); i++) {
//                Mineral pre = minerals.get(i - 1);
//                Mineral next = minerals.get(i);
//
//                if (pre.getStatus() != next.getStatus()) {
//                    falsePromise.died();
//                    pre.setCalculated(true);
//                    dao.reSketchVision(pre);
//                    continue;
//                }
//
//                long timeDifMinutes = TimeUnit.MILLISECONDS.toMinutes(
//                        Math.abs(next.getTimeStamp() - pre.getTimeStamp()));
//                float levelDif = Math.abs(next.getLevel() - pre.getLevel());
//                int remainingTime = (int) ((currentBatteryLevel * timeDifMinutes) / levelDif);
//
//                Vision foreSeen = new Vision(
//                        pre.getStatus(),
//                        currentBatteryLevel,
//                        remainingTime
//                );
//                pre.setCalculated(true);
//                dao.reSketchVision(pre);
//                dao.sketchForeSeen(foreSeen);
//            }


            //******************************************************************************************


//        Log.d(TAG, "doInBackground: " + dao.visionCount());
//
//        if (dao.visionCount() < 10) {
//            falsePromise.died();
//            return null;
//        }
//
//        List<Mineral> minerals = dao.getNotCalculatedMinerals();
//        Mineral mineral1 = minerals.get(11);
//        Mineral mineral2 = minerals.get(12);
//
//        long timeDifMinutes = TimeUnit.MILLISECONDS.toMinutes(
//                Math.abs(mineral1.getTimeStamp() - mineral2.getTimeStamp()));
//
//        Log.d(TAG, "doInBackground: " + timeDifMinutes);
//
//        float levelDif = Math.abs(mineral1.getLevel() - mineral2.getLevel());
//        Log.d(TAG, "doInBackground: " + levelDif);
//
//        float remainingTime = (currentBatteryLevel * timeDifMinutes) / levelDif;
//        Log.d(TAG, "doInBackground: " + remainingTime);
//        falsePromise.healed();
        }
    }

    public interface FalsePromise {
        default void healed(float remainingTimeMs) {}

        default void died() {}
    }
}