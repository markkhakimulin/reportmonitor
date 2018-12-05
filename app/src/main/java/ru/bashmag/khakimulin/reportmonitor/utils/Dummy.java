package ru.bashmag.khakimulin.reportmonitor.utils;

import java.util.ArrayList;
import java.util.Date;

import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.SalesData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportData;

/**
 * Created by Mark Khakimulin on 29.11.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class Dummy {

    public static ArrayList<TurnoverReportData> turnoverDummyContent() {
        ArrayList<TurnoverReportData> list = new ArrayList<>();
        list.add(new TurnoverReportData(
                new TurnoverData("5089e211-9547-44e5-8734-f77055d4a5e0", "Орехово", 12234.0f, 13355.0f, new Date()),
                new TurnoverData("5089e211-9547-44e5-8734-f77055d4a5e0", "Орехово", 551.0f, 125.0f, new Date()),
                new ConversionData("5089e211-9547-44e5-8734-f77055d4a5e0", "Орехово", 125, 25, 50, new Date())));
        list.add(new TurnoverReportData(
                new TurnoverData("a0a62659-e997-11e4-8b01-001e677b2cce", "Антилопа Дисконт", 4554.0f, 5665.0f, new Date()),
                new TurnoverData("a0a62659-e997-11e4-8b01-001e677b2cce", "Антилопа Дисконт", 322.0f, 455.0f, new Date()),
                new ConversionData("a0a62659-e997-11e4-8b01-001e677b2cce", "Антилопа Дисконт", 33, 15, 45, new Date())));
        list.add(new TurnoverReportData(
                new TurnoverData("3b091195-c451-4205-9595-43161b881461", "Электросталь", 666.0f, 333.0f, new Date()),
                new TurnoverData("3b091195-c451-4205-9595-43161b881461", "Электросталь", 120.0f, 150.0f, new Date()),
                new ConversionData("3b091195-c451-4205-9595-43161b881461", "Электросталь", 1204, 59, 88, new Date())));
        list.add(new TurnoverReportData(
                new TurnoverData("8408915c-3efe-477f-9646-d8c1a1dfcd6e", "Марьино", 666.0f, 333.0f, new Date()),
                new TurnoverData("8408915c-3efe-477f-9646-d8c1a1dfcd6e", "Марьино", 120.0f, 150.0f, new Date()),
                new ConversionData("8408915c-3efe-477f-9646-d8c1a1dfcd6e", "Марьино", 1204, 59, 87, new Date())));
        list.add(new TurnoverReportData(
                new TurnoverData("3a96fa3f-b8b7-4531-96f2-b26f3ff5face", "Ппсад", 666.0f, 333.0f, new Date()),
                new TurnoverData("3a96fa3f-b8b7-4531-96f2-b26f3ff5face", "Ппсад", 120.0f, 150.0f, new Date()),
                new ConversionData("3a96fa3f-b8b7-4531-96f2-b26f3ff5face", "Ппсад", 1204, 59, 66, new Date())));
        list.add(new TurnoverReportData(
                new TurnoverData("1bc92061-1eb9-11e7-9f04-001e67a839ff", "Косино", 666.0f, 333.0f, new Date()),
                new TurnoverData("1bc92061-1eb9-11e7-9f04-001e67a839ff", "Косино", 120.0f, 150.0f, new Date()),
                new ConversionData("1bc92061-1eb9-11e7-9f04-001e67a839ff", "Косино", 1204, 59, 66, new Date())));
        list.add(new TurnoverReportData(
                new TurnoverData("ce286069-e197-4a5a-a4b7-5947ba3d211a", "Лыткарино", 666.0f, 333.0f, new Date()),
                new TurnoverData("ce286069-e197-4a5a-a4b7-5947ba3d211a", "Лыткарино", 120.0f, 150.0f, new Date()),
                new ConversionData("ce286069-e197-4a5a-a4b7-5947ba3d211a", "Лыткарино", 1204, 59, 99, new Date())));
        list.add(new TurnoverReportData(
                new TurnoverData("8eec27b2-5426-4d1d-a732-ec5417a590c9", "Молостовых", 666.0f, 333.0f, new Date()),
                new TurnoverData("8eec27b2-5426-4d1d-a732-ec5417a590c9", "Молостовых", 120.0f, 150.0f, new Date()),
                new ConversionData("8eec27b2-5426-4d1d-a732-ec5417a590c9", "Молостовых", 1204, 59, 99, new Date())));
        return list;
    }

    public static ArrayList<SalesData> salesDummyContent() {
        ArrayList<SalesData> list = new ArrayList<>();
        list.add(new SalesData("5089e211-9547-44e5-8734-f77055d4a5e0","Орехово","Алеша","Продавец",new TurnoverData[]{
                new TurnoverData("5089e211-9547-44e5-8734-f77055d4a5e0","Орехово",12234,13355,"Обувь"),
                new TurnoverData("5089e211-9547-44e5-8734-f77055d4a5e0","Орехово",551,125,"Аксессуары"),
                new TurnoverData("5089e211-9547-44e5-8734-f77055d4a5e0","Орехово",551,125,"Всякая хрень")}));
        list.add(new SalesData("a0a62659-e997-11e4-8b01-001e677b2cce","Антилопа Дисконт","Алеша","Продавец",new TurnoverData[]{
                new TurnoverData("a0a62659-e997-11e4-8b01-001e677b2cce","Антилопа Дисконт",12234,13355,"Обувь"),
                new TurnoverData("a0a62659-e997-11e4-8b01-001e677b2cce","Антилопа Дисконт",551,125,"Аксессуары"),
                new TurnoverData("a0a62659-e997-11e4-8b01-001e677b2cce","Антилопа Дисконт",551,125,new Date())}));
        list.add(new SalesData("3b091195-c451-4205-9595-43161b881461","Электросталь","Алеша","Продавец",new TurnoverData[]{
                new TurnoverData("3b091195-c451-4205-9595-43161b881461","Электросталь",12234,13355,"Обувь"),
                new TurnoverData("3b091195-c451-4205-9595-43161b881461","Электросталь",551,125,"Аксессуары"),
                new TurnoverData("3b091195-c451-4205-9595-43161b881461","Электросталь",551,125,"Всякая хрень")}));
        list.add(new SalesData("8408915c-3efe-477f-9646-d8c1a1dfcd6e","Марьино","Алеша","Продавец",new TurnoverData[]{
                new TurnoverData("8408915c-3efe-477f-9646-d8c1a1dfcd6e","Марьино",12234,13355,"Обувь"),
                new TurnoverData("8408915c-3efe-477f-9646-d8c1a1dfcd6e","Марьино",551,125,"Аксессуары"),
                new TurnoverData("8408915c-3efe-477f-9646-d8c1a1dfcd6e","Марьино",551,125,"Всякая хрень")}));
        list.add(new SalesData("3a96fa3f-b8b7-4531-96f2-b26f3ff5face","Ппсад","Алеша","Продавец",new TurnoverData[]{
                new TurnoverData("3a96fa3f-b8b7-4531-96f2-b26f3ff5face","Ппсад",12234,13355,"Обувь"),
                new TurnoverData("3a96fa3f-b8b7-4531-96f2-b26f3ff5face","Ппсад",551,125,"Аксессуары"),
                new TurnoverData("3a96fa3f-b8b7-4531-96f2-b26f3ff5face","Ппсад",551,125,"Всякая хрень")}));
        return list;
    }

}
