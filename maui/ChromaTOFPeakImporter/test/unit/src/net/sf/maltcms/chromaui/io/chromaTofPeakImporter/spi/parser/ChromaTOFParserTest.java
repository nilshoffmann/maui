/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
package net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.parser;

import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IVariableFragment;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.Assert;
import maltcms.datastructures.ms.IChromatogram;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.junit.Test;
import static org.junit.Assert.*;
import ucar.ma2.Array;

/**
 *
 * @author Nils Hoffmann
 */
public class ChromaTOFParserTest {

    /**
     * Test of splitLine method, of class ChromaTOFParser.
     */
    @Test
    public void testSplitLine() {
        System.out.println("splitLine");
        String line = "\"EITTMS_N12C_ATHR_1808.9_1164CC59_[737; Gluconic acid (6TMS)]\",\"\",\"1890 , 1.710\",\"680 , 2.560,4373\",73:970 147:263 75:128 117:126 86:84 133:70 59:62 174:61 74:48 57:40 55:35 66:32 103:31 234:28 205:27 129:26 191:24 83:23 116:23 69:22 100:18 149:18 131:17 217:15 166:15 499:14 306:14 72:13 458:13 143:13 130:12 231:11 60:11 245:10 97:10 148:10 175:9 218:9 229:9 360:9 71:9 292:9 335:9 731:8 585:8 206:8 178:7 63:7 254:7 307:7 445:7 515:7 548:7 422:7 159:7 517:6 257:6 201:6 101:6 157:6 709:6 173:5 750:5 440:5 309:5 639:5 378:5 591:5 350:5 243:5 465:5 459:5 429:4 279:4 486:4 115:4 398:4 56:4 699:4 353:4 547:4 539:4 472:4 263:4 510:4 727:4 673:3 533:3 462:3 467:3 313:3 677:3 291:3 747:3 448:3 501:3 420:3 728:3 176:2 492:2 464:2 233:2 163:2 288:2 600:1 717:1 128:1 577:1 215:1 315:1 733:1 375:1 180:1 84:0 125:0 145:0 126:0 124:0 146:0 85:0 106:0 107:0 64:0 127:0 169:0 78:0 123:0 156:0 168:0 94:0 51:0 139:0 53:0 65:0 81:0 67:0 186:0 187:0 188:0 102:0 190:0 170:0 171:0 193:0 153:0 164:0 196:0 155:0 177:0 91:0 92:0 137:0 181:0 182:0 194:0 195:0 99:0 120:0 121:0 209:0 210:0 211:0 212:0 105:0 214:0 172:0 216:0 87:0 110:0 111:0 199:0 221:0 135:0 93:0 224:0 96:0 141:0 77:0 228:0 54:0 230:0 144:0 232:0 58:0 213:0 61:0 62:0 150:0 238:0 239:0 240:0 220:0 242:0 68:0 244:0 158:0 225:0 226:0 52:0 249:0 76:0 251:0 252:0 253:0 80:0 255:0 256:0 82:0 258:0 259:0 260:0 261:0 154:0 89:0 264:0 265:0 179:0 267:0 268:0 162:0 270:0 271:0 272:0 273:0 274:0 275:0 276:0 277:0 278:0 104:0 280:0 281:0 282:0 283:0 284:0 285:0 286:0 287:0 113:0 246:0 269:0 161:0 118:0 119:0 207:0 295:0 296:0 297:0 298:0 299:0 300:0 301:0 302:0 303:0 304:0 305:0 219:0 132:0 308:0 134:0 310:0 311:0 312:0 138:0 314:0 140:0 316:0 317:0 318:0 319:0 320:0 321:0 322:0 236:0 237:0 325:0 326:0 152:0 328:0 329:0 330:0 331:0 332:0 333:0 334:0 248:0 336:0 337:0 338:0 339:0 340:0 341:0 342:0 343:0 344:0 345:0 346:0 347:0 348:0 262:0 88:0 351:0 352:0 266:0 354:0 355:0 356:0 357:0 358:0 184:0 185:0 361:0 362:0 363:0 364:0 365:0 366:0 192:0 368:0 369:0 370:0 371:0 372:0 373:0 374:0 200:0 376:0 202:0 204:0 379:0 293:0 294:0 382:0 208:0 384:0 385:0 386:0 387:0 388:0 389:0 390:0 391:0 392:0 393:0 394:0 395:0 396:0 222:0 223:0 399:0 50:0 401:0 402:0 403:0 404:0 405:0 406:0 407:0 408:0 409:0 410:0 411:0 412:0 413:0 414:0 415:0 416:0 417:0 418:0 419:0 70:0 421:0 247:0 423:0 424:0 425:0 426:0 427:0 428:0 79:0 430:0 431:0 432:0 433:0 434:0 435:0 436:0 437:0 438:0 439:0 90:0 441:0 442:0 443:0 444:0 95:0 446:0 447:0 98:0 449:0 450:0 451:0 452:0 453:0 454:0 455:0 456:0 457:0 108:0 109:0 460:0 461:0 112:0 463:0 114:0 290:0 466:0 380:0 468:0 469:0 470:0 471:0 122:0 473:0 474:0 475:0 476:0 477:0 478:0 479:0 480:0 481:0 482:0 483:0 484:0 485:0 136:0 487:0 488:0 489:0 490:0 491:0 142:0 493:0 494:0 495:0 496:0 497:0 498:0 324:0 500:0 151:0 502:0 503:0 504:0 505:0 506:0 507:0 508:0 509:0 160:0 511:0 512:0 513:0 514:0 165:0 516:0 167:0 518:0 519:0 520:0 521:0 522:0 523:0 524:0 525:0 526:0 527:0 528:0 529:0 530:0 531:0 532:0 183:0 534:0 535:0 536:0 537:0 538:0 189:0 540:0 541:0 542:0 543:0 544:0 545:0 546:0 197:0 198:0 549:0 550:0 551:0 552:0 203:0 554:0 555:0 556:0 557:0 558:0 559:0 560:0 561:0 562:0 563:0 564:0 565:0 566:0 567:0 568:0 569:0 570:0 571:0 572:0 573:0 574:0 575:0 576:0 227:0 578:0 579:0 580:0 581:0 582:0 583:0 584:0 235:0 586:0 587:0 588:0 589:0 590:0 241:0 592:0 593:0 594:0 595:0 596:0 597:0 598:0 599:0 250:0 601:0 602:0 603:0 604:0 605:0 606:0 607:0 608:0 609:0 610:0 611:0 612:0 613:0 614:0 615:0 616:0 617:0 618:0 619:0 620:0 621:0 622:0 623:0 624:0 625:0 626:0 627:0 628:0 629:0 630:0 631:0 632:0 633:0 634:0 635:0 636:0 637:0 638:0 289:0 640:0 641:0 642:0 643:0 644:0 645:0 646:0 647:0 648:0 649:0 650:0 651:0 652:0 653:0 654:0 655:0 656:0 657:0 658:0 659:0 660:0 661:0 662:0 663:0 664:0 665:0 666:0 667:0 668:0 669:0 670:0 671:0 672:0 323:0 674:0 675:0 676:0 327:0 678:0 679:0 680:0 681:0 682:0 683:0 684:0 685:0 686:0 687:0 688:0 689:0 690:0 691:0 692:0 693:0 694:0 695:0 696:0 697:0 698:0 349:0 700:0 701:0 702:0 703:0 704:0 705:0 706:0 707:0 708:0 359:0 710:0 711:0 712:0 713:0 714:0 715:0 716:0 367:0 718:0 719:0 720:0 721:0 722:0 723:0 724:0 725:0 726:0 377:0 553:0 729:0 730:0 381:0 732:0 383:0 734:0 735:0 736:0 737:0 738:0 739:0 740:0 741:0 742:0 743:0 744:0 745:0 746:0 397:0 748:0 749:0 400:0";
        //String line = "\"A \",\" B,CD\",E:F G:H";//,\"BCD\"";
        String fieldSeparator = ",";
        String quoteSymbol = "\"";
        System.out.println(line);
        System.out.println("Quote symbol: "+quoteSymbol);
        String[] expResult = new String[]{"EITTMS_N12C_ATHR_1808.9_1164CC59_[737; Gluconic acid (6TMS)]","","1890 , 1.710","680 , 2.560,4373","73:970 147:263 75:128 117:126 86:84 133:70 59:62 174:61 74:48 57:40 55:35 66:32 103:31 234:28 205:27 129:26 191:24 83:23 116:23 69:22 100:18 149:18 131:17 217:15 166:15 499:14 306:14 72:13 458:13 143:13 130:12 231:11 60:11 245:10 97:10 148:10 175:9 218:9 229:9 360:9 71:9 292:9 335:9 731:8 585:8 206:8 178:7 63:7 254:7 307:7 445:7 515:7 548:7 422:7 159:7 517:6 257:6 201:6 101:6 157:6 709:6 173:5 750:5 440:5 309:5 639:5 378:5 591:5 350:5 243:5 465:5 459:5 429:4 279:4 486:4 115:4 398:4 56:4 699:4 353:4 547:4 539:4 472:4 263:4 510:4 727:4 673:3 533:3 462:3 467:3 313:3 677:3 291:3 747:3 448:3 501:3 420:3 728:3 176:2 492:2 464:2 233:2 163:2 288:2 600:1 717:1 128:1 577:1 215:1 315:1 733:1 375:1 180:1 84:0 125:0 145:0 126:0 124:0 146:0 85:0 106:0 107:0 64:0 127:0 169:0 78:0 123:0 156:0 168:0 94:0 51:0 139:0 53:0 65:0 81:0 67:0 186:0 187:0 188:0 102:0 190:0 170:0 171:0 193:0 153:0 164:0 196:0 155:0 177:0 91:0 92:0 137:0 181:0 182:0 194:0 195:0 99:0 120:0 121:0 209:0 210:0 211:0 212:0 105:0 214:0 172:0 216:0 87:0 110:0 111:0 199:0 221:0 135:0 93:0 224:0 96:0 141:0 77:0 228:0 54:0 230:0 144:0 232:0 58:0 213:0 61:0 62:0 150:0 238:0 239:0 240:0 220:0 242:0 68:0 244:0 158:0 225:0 226:0 52:0 249:0 76:0 251:0 252:0 253:0 80:0 255:0 256:0 82:0 258:0 259:0 260:0 261:0 154:0 89:0 264:0 265:0 179:0 267:0 268:0 162:0 270:0 271:0 272:0 273:0 274:0 275:0 276:0 277:0 278:0 104:0 280:0 281:0 282:0 283:0 284:0 285:0 286:0 287:0 113:0 246:0 269:0 161:0 118:0 119:0 207:0 295:0 296:0 297:0 298:0 299:0 300:0 301:0 302:0 303:0 304:0 305:0 219:0 132:0 308:0 134:0 310:0 311:0 312:0 138:0 314:0 140:0 316:0 317:0 318:0 319:0 320:0 321:0 322:0 236:0 237:0 325:0 326:0 152:0 328:0 329:0 330:0 331:0 332:0 333:0 334:0 248:0 336:0 337:0 338:0 339:0 340:0 341:0 342:0 343:0 344:0 345:0 346:0 347:0 348:0 262:0 88:0 351:0 352:0 266:0 354:0 355:0 356:0 357:0 358:0 184:0 185:0 361:0 362:0 363:0 364:0 365:0 366:0 192:0 368:0 369:0 370:0 371:0 372:0 373:0 374:0 200:0 376:0 202:0 204:0 379:0 293:0 294:0 382:0 208:0 384:0 385:0 386:0 387:0 388:0 389:0 390:0 391:0 392:0 393:0 394:0 395:0 396:0 222:0 223:0 399:0 50:0 401:0 402:0 403:0 404:0 405:0 406:0 407:0 408:0 409:0 410:0 411:0 412:0 413:0 414:0 415:0 416:0 417:0 418:0 419:0 70:0 421:0 247:0 423:0 424:0 425:0 426:0 427:0 428:0 79:0 430:0 431:0 432:0 433:0 434:0 435:0 436:0 437:0 438:0 439:0 90:0 441:0 442:0 443:0 444:0 95:0 446:0 447:0 98:0 449:0 450:0 451:0 452:0 453:0 454:0 455:0 456:0 457:0 108:0 109:0 460:0 461:0 112:0 463:0 114:0 290:0 466:0 380:0 468:0 469:0 470:0 471:0 122:0 473:0 474:0 475:0 476:0 477:0 478:0 479:0 480:0 481:0 482:0 483:0 484:0 485:0 136:0 487:0 488:0 489:0 490:0 491:0 142:0 493:0 494:0 495:0 496:0 497:0 498:0 324:0 500:0 151:0 502:0 503:0 504:0 505:0 506:0 507:0 508:0 509:0 160:0 511:0 512:0 513:0 514:0 165:0 516:0 167:0 518:0 519:0 520:0 521:0 522:0 523:0 524:0 525:0 526:0 527:0 528:0 529:0 530:0 531:0 532:0 183:0 534:0 535:0 536:0 537:0 538:0 189:0 540:0 541:0 542:0 543:0 544:0 545:0 546:0 197:0 198:0 549:0 550:0 551:0 552:0 203:0 554:0 555:0 556:0 557:0 558:0 559:0 560:0 561:0 562:0 563:0 564:0 565:0 566:0 567:0 568:0 569:0 570:0 571:0 572:0 573:0 574:0 575:0 576:0 227:0 578:0 579:0 580:0 581:0 582:0 583:0 584:0 235:0 586:0 587:0 588:0 589:0 590:0 241:0 592:0 593:0 594:0 595:0 596:0 597:0 598:0 599:0 250:0 601:0 602:0 603:0 604:0 605:0 606:0 607:0 608:0 609:0 610:0 611:0 612:0 613:0 614:0 615:0 616:0 617:0 618:0 619:0 620:0 621:0 622:0 623:0 624:0 625:0 626:0 627:0 628:0 629:0 630:0 631:0 632:0 633:0 634:0 635:0 636:0 637:0 638:0 289:0 640:0 641:0 642:0 643:0 644:0 645:0 646:0 647:0 648:0 649:0 650:0 651:0 652:0 653:0 654:0 655:0 656:0 657:0 658:0 659:0 660:0 661:0 662:0 663:0 664:0 665:0 666:0 667:0 668:0 669:0 670:0 671:0 672:0 323:0 674:0 675:0 676:0 327:0 678:0 679:0 680:0 681:0 682:0 683:0 684:0 685:0 686:0 687:0 688:0 689:0 690:0 691:0 692:0 693:0 694:0 695:0 696:0 697:0 698:0 349:0 700:0 701:0 702:0 703:0 704:0 705:0 706:0 707:0 708:0 359:0 710:0 711:0 712:0 713:0 714:0 715:0 716:0 367:0 718:0 719:0 720:0 721:0 722:0 723:0 724:0 725:0 726:0 377:0 553:0 729:0 730:0 381:0 732:0 383:0 734:0 735:0 736:0 737:0 738:0 739:0 740:0 741:0 742:0 743:0 744:0 745:0 746:0 397:0 748:0 749:0 400:0"};
        //String[] expResult = new String[]{"A","B,CD","E:F G:H"};//,"BCD"};
        Pattern p = Pattern.compile("((\")([^\"]*)(\"))");
        Matcher m = p.matcher(line);
        List<String> results = new LinkedList<String>();
        int match = 1;
        while(m.find()) {
            System.out.println("Match "+(match++));
            for(int i = 0;i<m.groupCount();i++){
                System.out.println("Group "+i+": "+m.group(i));
            }
            results.add(m.group(3).trim());
        }
        Pattern endPattern = Pattern.compile(fieldSeparator+"([\"]{0,1}([^\"]*)[^\"]{0,1}$)");
        Matcher m2 = endPattern.matcher(line);
        while(m2.find()) {
            for(int i = 0;i<m2.groupCount();i++) {
                System.out.println("Group "+i+": "+m2.group(i));
            }
            results.add(m2.group(1).trim());
        }
        String[] result = results.toArray(new String[results.size()]);
        System.out.println("Expected: "+Arrays.toString(expResult));
        System.out.println("Got: "+Arrays.toString(result));
        //String[] result = ChromaTOFParser.splitLine(line, fieldSeparator, quoteSymbol);
        assertArrayEquals(expResult, result);
    }

}
