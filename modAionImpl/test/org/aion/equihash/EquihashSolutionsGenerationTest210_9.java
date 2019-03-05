package org.aion.equihash;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.aion.util.TestResources;
import org.aion.zero.impl.types.AionBlock;
import org.aion.zero.types.A0BlockHeader;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class EquihashSolutionsGenerationTest210_9 {
    private int n = 210;
    private int k = 9;

    @Test
    public void ZeroSolutionTest() {
        byte[] header = {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0
        };

        byte[] nonce = {
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
        };

        Equihash e = new Equihash(n, k);

        int[][] sol = e.getSolutionsForNonce(header, nonce);

        assertEquals(sol.length, 0);
    }

    @Test
    public void TwoSolutionTest() {
        byte[] header = {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0
        };

        byte[] nonce = {
            1, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
        };

        Equihash e = new Equihash(n, k);

        int[][] sol = e.getSolutionsForNonce(header, nonce);

        int[] expectedSol1 = {
            20355, 2545802, 907342, 1930412,
            905311, 3877411, 1903231, 2689731,
            506861, 2315704, 1951052, 2227153,
            2777366, 3236666, 3589074, 3643676,
            504683, 1356644, 1438735, 3811518,
            2217209, 3942573, 3417766, 3756123,
            678546, 1762659, 2764197, 4043088,
            837400, 3860117, 845468, 2155675,
            158339, 1624003, 296319, 2896689,
            1890905, 2739467, 2400006, 2772678,
            465854, 833805, 512180, 1188591,
            994659, 2860954, 1118590, 1430454,
            468682, 2389668, 1777223, 2936757,
            482455, 2375862, 1500518, 1974003,
            658874, 3347208, 1725007, 2957767,
            1148912, 3836887, 2679675, 3926943,
            34780, 2564574, 2390936, 2555613,
            946022, 3803691, 2314033, 4106977,
            1433928, 2898030, 1534277, 1818947,
            2262862, 4100511, 2705431, 4040332,
            270150, 3015764, 1312138, 3075144,
            1080444, 2162225, 2959308, 3157745,
            385118, 2198870, 2851713, 3714102,
            515881, 3397675, 2382411, 2444766,
            48996, 3898535, 3129497, 3693976,
            95695, 2435387, 2911079, 3718581,
            578995, 1981490, 1002209, 2070152,
            859812, 929850, 1206926, 4153868,
            203330, 2339854, 2964095, 3552937,
            842873, 3698309, 2497048, 3769276,
            584594, 1264798, 1933308, 3523328,
            1283227, 2242647, 2190815, 3020750,
            41390, 3771880, 263000, 758924,
            559478, 3168011, 1303488, 3381537,
            717900, 4072531, 1661229, 3319094,
            1127203, 2119989, 1850536, 2099166,
            121852, 2843305, 566396, 1051840,
            791008, 2612111, 1570629, 2709103,
            812775, 3641315, 1053858, 1492834,
            2578414, 3127037, 2987191, 3276177,
            183924, 1831245, 878825, 2604335,
            1595819, 4145284, 1610183, 3268750,
            448042, 1999222, 1006093, 2711058,
            2788669, 3201236, 2905205, 4191295,
            263045, 288816, 708465, 3818877,
            477879, 3014614, 2424026, 3730921,
            268146, 1481166, 1687705, 1851825,
            1115358, 4087438, 2834812, 3655853,
            142275, 2932955, 2978299, 3315362,
            866230, 1167961, 1666141, 3445169,
            1076566, 1877713, 2740346, 3081165,
            1684805, 2848657, 1961158, 3927792,
            424919, 2869909, 1390027, 2948742,
            672678, 1305311, 2335716, 2408750,
            1656898, 2315900, 2228009, 3897974,
            2080083, 3358645, 2183017, 2474962,
            227904, 3257048, 1015946, 2246708,
            744733, 4046023, 1305041, 4000191,
            597568, 3788490, 1629963, 3055481,
            1203517, 1878102, 2311148, 3069747,
            475712, 2736079, 2093117, 2709022,
            1766719, 2760187, 2308251, 3000603,
            1122895, 4052370, 2081446, 2580232,
            1497222, 1861105, 2240286, 2531758,
            30071, 2842909, 590630, 3979760,
            696925, 3258522, 1514676, 3716048,
            1030285, 1986957, 2226931, 2864777,
            2455350, 3168779, 3066230, 3403299,
            256066, 2499634, 3560516, 4094756,
            1256581, 2305277, 1782337, 4079485,
            426456, 2259473, 464202, 3688991,
            1861389, 3444866, 2960951, 3186120,
            96490, 3786348, 912465, 2189746,
            3408757, 3465881, 3510320, 4162223,
            371697, 1169844, 2378665, 3735875,
            1591695, 1866557, 1893808, 2941633,
            754250, 3548696, 1247845, 3450148,
            1067081, 1578401, 1486825, 3237627,
            767078, 3201793, 1632439, 2521903,
            1137703, 2456425, 1407972, 2485769,
            160330, 3614857, 1481210, 1578079,
            1996927, 4075979, 2984588, 3931673,
            743440, 1237620, 1744748, 3133823,
            1673449, 2727191, 1793757, 4002209,
            200923, 1287230, 1452788, 1850912,
            1456000, 4019882, 1605453, 3160917,
            386477, 3366002, 3231682, 3735723,
            491335, 4153328, 1244051, 2764520,
            177653, 2136121, 1266815, 1362612,
            1486929, 4194196, 3374899, 3540741,
            220240, 2833638, 1866174, 3835077,
            669705, 1597154, 1154067, 2212093,
            317749, 3488031, 3363506, 3598838,
            2808761, 3451142, 3535535, 3937125,
            907483, 4099106, 1958727, 3318901,
            2551208, 3087618, 2600877, 3702862,
            44753, 3160552, 456566, 2367674,
            1691363, 3123342, 3187852, 3760152,
            1103888, 2963337, 3512868, 3825332,
            1146648, 1433039, 3309183, 3492584,
            207921, 3987717, 251562, 3497242,
            3207164, 4065140, 3998081, 4109968,
            584588, 3212329, 774474, 3481768,
            2096006, 3238743, 3115602, 3803926,
            66244, 309456, 640355, 2060813,
            877456, 1728849, 1902807, 2306919,
            289666, 4152610, 2712657, 3324726,
            599498, 1425451, 1839380, 2385592,
            705670, 3056467, 2670990, 2847010,
            1188781, 2812793, 1769251, 3789434,
            1898743, 2446962, 2867004, 3715274,
            2354821, 3601552, 3140000, 3578421,
            140339, 598398, 606801, 1294404,
            1806336, 1986146, 2500515, 3164517,
            1599048, 2800323, 3361171, 3678605,
            1606340, 1901611, 2081741, 2494676,
            821973, 3172246, 1760974, 3682564,
            1307586, 1596950, 1339018, 1768126,
            1033573, 2310148, 1821481, 2445292,
            1948298, 2221343, 2225320, 3313311,
            160437, 1281935, 386292, 3121792,
            944232, 1247064, 2646561, 2904304,
            546854, 4051902, 664040, 2880916,
            1721878, 2042202, 3076451, 4054925,
            232459, 3321779, 389694, 2063343,
            920162, 3452042, 3197977, 4166342,
            452733, 4016722, 2713436, 3119993,
            913404, 2140622, 1655092, 2472993
        };

        int[] expectedSol2 = {
            18355, 3904886, 158770, 462925,
            1024030, 1815873, 1530041, 3183543,
            282499, 2526613, 789924, 3098715,
            889754, 1035548, 927374, 1985705,
            112018, 3953975, 1878795, 4154215,
            638587, 4179611, 2277590, 3515874,
            395312, 3804842, 2410209, 3063447,
            426023, 3928196, 624692, 934963,
            85228, 2330285, 211105, 4183387,
            929548, 2652970, 1263899, 1390987,
            630475, 2376997, 2652548, 3794596,
            1134523, 3954679, 3579940, 4008579,
            565507, 2240932, 1507220, 4075077,
            734400, 2361912, 3599383, 3686153,
            1353886, 4183609, 2337069, 3657064,
            3318827, 3680116, 3393520, 3614744,
            39309, 3975278, 2411839, 3848792,
            1534654, 2506872, 3002394, 3364270,
            788002, 1851874, 1653336, 4028425,
            896675, 2906239, 2033724, 2128553,
            112715, 1735514, 279350, 1987599,
            565647, 1345285, 3219735, 3533932,
            146942, 1437259, 494634, 2791849,
            949167, 4088491, 2107022, 2655048,
            83181, 2713891, 910705, 2267921,
            320061, 1554293, 1808157, 3791443,
            1838199, 4006352, 2232675, 3405006,
            2074606, 2724094, 2986831, 3633629,
            1020318, 3548554, 2854848, 3697983,
            1198053, 3249573, 1234276, 3198845,
            1048501, 1202435, 3573739, 3893718,
            1481054, 3863058, 3826569, 4097066,
            127748, 162842, 1813681, 2449946,
            380609, 2398297, 641086, 2918602,
            349698, 1523487, 1418276, 3660971,
            2090786, 3973992, 2452216, 3322568,
            526936, 4168246, 1595548, 2988246,
            660981, 1140257, 1765716, 3801002,
            1371268, 1919016, 1674122, 2849774,
            1774122, 3975907, 2216028, 4068646,
            176142, 2943303, 1634098, 2537857,
            497268, 2077215, 1793649, 3527373,
            317337, 3388765, 1612827, 3186235,
            468535, 915382, 2859556, 3844531,
            810635, 2887392, 916260, 1876228,
            1371801, 1984909, 2514887, 3186953,
            1933055, 2803573, 2320954, 2857842,
            2213182, 3839944, 2265198, 2649090,
            199097, 633333, 1432493, 1622390,
            1170038, 2251561, 1350306, 1583435,
            1237030, 2702497, 1520571, 1676053,
            1394825, 2351426, 2472728, 3525862,
            758826, 3015313, 2775964, 3451946,
            1030771, 1368041, 1711817, 3722841,
            1046141, 3010602, 2348050, 3193263,
            2929566, 3001436, 3663970, 3972727,
            316210, 1428146, 1631131, 3122862,
            499766, 2273375, 733615, 3019606,
            597902, 3027938, 1243932, 3218828,
            1890105, 3295681, 2377644, 3980845,
            338830, 1135556, 2039974, 2573124,
            391053, 4167748, 1706103, 1859184,
            416968, 3283262, 1460635, 3093235,
            452883, 3442799, 1663173, 2616169,
            77008, 2682826, 203026, 2854167,
            831176, 2619607, 2116828, 2732152,
            90842, 2838853, 162895, 3495476,
            126660, 3806969, 819083, 3947167,
            316473, 3049358, 2998928, 3716162,
            2705374, 4042326, 2748895, 2828497,
            723128, 2129724, 3634183, 3980645,
            797296, 2627702, 1647500, 3384292,
            93116, 2966884, 2817902, 2967991,
            374585, 1824689, 3565901, 4131249,
            193781, 2206418, 1433874, 2888607,
            991406, 2727800, 1906550, 3124080,
            129500, 1664500, 3120329, 3504246,
            400175, 795158, 2107609, 4090593,
            351095, 2827991, 2527959, 3822979,
            1456173, 3834822, 2707651, 3183572,
            203074, 620391, 1813660, 2635938,
            204272, 516420, 2034497, 3885708,
            598870, 3427902, 2555063, 3279764,
            674666, 2998196, 2213325, 3000603,
            320898, 4186147, 2438192, 3874771,
            2458349, 3152994, 3318989, 3662735,
            641297, 2539043, 3154370, 3425794,
            723929, 2595770, 1894694, 2622202,
            293303, 3928471, 748822, 3736731,
            863208, 3128440, 1468663, 2205559,
            771851, 1674042, 1295802, 2797125,
            2577072, 2871472, 3111421, 3468934,
            493759, 3554207, 3572479, 3830977,
            590764, 2909942, 3352434, 3954561,
            1727561, 2490228, 2111169, 2959162,
            2573969, 3850866, 2614844, 4047649,
            90569, 3553510, 1103305, 1932060,
            1241414, 1392707, 1776223, 4091400,
            784094, 1152077, 1440818, 2051658,
            1793025, 1935754, 1896320, 3920699,
            241949, 3983093, 327564, 3978268,
            263260, 1308863, 1268934, 3695791,
            417056, 2392864, 2537286, 3943810,
            431636, 1187970, 2308207, 2986617,
            111392, 2905636, 2172751, 2745550,
            1957093, 4010858, 3606795, 4158520,
            188127, 429397, 2142667, 2818744,
            665596, 3666319, 1445651, 2452730,
            234753, 3253528, 1916956, 3314574,
            1397999, 2256974, 1903199, 3845654,
            418356, 913304, 3778229, 3941365,
            931880, 4021164, 1571789, 2311709,
            203602, 3197270, 847481, 3065930,
            600565, 841479, 3816877, 4187769,
            760734, 2883879, 2622835, 3335186,
            863012, 3622791, 2723309, 3547059,
            469300, 948464, 956516, 3167755,
            1232435, 4077417, 3099649, 3667776,
            1162411, 2019965, 1529155, 3320976,
            1671418, 3185460, 3256787, 3660861,
            635078, 642951, 2984677, 3969996,
            819529, 3788287, 1611060, 3312071,
            1119879, 4053923, 1498598, 3020491,
            1532928, 3073299, 3116504, 3371418,
            674344, 3251269, 2266812, 2738528,
            1671573, 3775908, 1848816, 3534999,
            1044399, 3623389, 1709105, 1728011,
            1112706, 1422473, 2869993, 3662545
        };

        assertEquals(sol.length, 2);
        assertArrayEquals(sol[0], expectedSol1);
        assertArrayEquals(sol[1], expectedSol2);
    }

    @Test
    @Parameters(method = "blocks")
    public void testMine_wBlockData(AionBlock block) {

        A0BlockHeader header = block.getHeader();

        Equihash spy = spy(new Equihash(n, k));

        // mock return the known solution
        when(spy.getSolutionsForNonce(header.getMineHash(), header.getNonce()))
                .thenReturn(
                        new int[][] {
                            EquiUtils.getIndicesFromMinimal(header.getSolution(), n / (k + 1))
                        });

        // use real method for mine call
        when(spy.mine(block, header.getNonce())).thenCallRealMethod();

        AionPowSolution sol = spy.mine(block, block.getNonce());

        assertNotNull(sol);
        assertArrayEquals(header.getNonce(), sol.getNonce());
        assertArrayEquals(header.getSolution(), sol.getSolution());
        assertArrayEquals(header.getMineHash(), sol.getBlock().getHeader().getMineHash());
    }

    public static Object blocks() {
        return TestResources.blocks(10);
    }
}
