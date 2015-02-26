import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

import persistence.MongoDBManager;
import play.Application;
import play.GlobalSettings;
import play.cache.Cache;
import play.libs.Akka;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Result;
import scala.collection.mutable.Set;
import scala.concurrent.duration.Duration;
import securesocial.core.Identity;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import models.users.UserService;
//import models.users.UserType;
import models.users.UserType;


public class Global extends GlobalSettings
{
	public long twoDays = 1000*60*60*48;
	public long week = 1000*60*60*24*7;
	private static int productCheckBatchSize;
	public static long dripfeedMinimum;
	public static long productNumMinimum;
	public static final long threshold = 50;
	static {
		try {
	productCheckBatchSize = ConfigFactory.load().getInt("extractor.products_check_batch_size");
		}catch(ConfigException e) {
			Config config = ConfigFactory.parseFile(new File("conf/application.conf")).resolve();
			productCheckBatchSize = config.getInt("extractor.products_check_batch_size");
		}
	}
	@Override
	public void onStop(Application application) {
		
	}
	@Override
	public void onStart(Application application)
	{  
		//StripeWrapper.getInstance().buy(product.get(0), o, "");
		dripfeedMinimum = System.currentTimeMillis();
		Akka.system().scheduler().schedule(  
				Duration.create(0, TimeUnit.MINUTES),
				Duration.create(5, TimeUnit.MINUTES),
				new Runnable() {
					@Override
					public void run() {
						
					}
				}, Akka.system().dispatcher());
	
		//List<Site> sites = new ArrayList<Site>();
		//sites.add(Site.findByUrl("http://www.trunkclothiers.com/"));//http://www.layerslondon.com/"));
		//sites.add(Site.findByUrl("http://www.youmustcreate.com/"));//http://www.layerslondon.com/"));
		/*Site ss = Site.findByUrl("http://www.folkclothing.com/");
		ss.addSeed("http://www.folkclothing.com/men/");
		ss.addSeed("http://www.folkclothing.com/women/");
		MongoDBManager.getInstance().update(ss);
		sites.add(ss);//http://www.layerslondon.com/"));
		*/
		//sites.addAll(Site.getAllScrapable());
		//sites.add(Site.findByUrl("http://www.layerslondon.com/"));//http://www.layerslondon.com/"));
		//sites.add(Site.findByUrl("http://www.verticelondon.com/"));
		//sites.add(Site.findByUrl("http://shop.doverstreetmarket.com/"));
		//sites.add(Site.findByUrl("http://www.slamcity.com/"));
		//ss.addSeed("http://shop.doverstreetmarket.com/");
		//MongoDBManager.getInstance().update(ss);
		//sites.add(ss);
	//	sites.add(Site.findByUrl("http://www.matchesfashion.com/"));
		/*HashSet<String> set = new HashSet<String>();
		HashSet<String> set2 = new HashSet<String>();
		set2.add("http://www.trunkclothiers.com/latest");
			sites.get(0).setHubs(set);
			sites.get(0).setSeeds(set2);
		MongoDBManager.getInstance().update(sites.get(0));*/
	/*	int number = 0;
		for (final Site s : sites) {
			Long lastScrape = s.getLastScrapedLong();
			Long interval = s.getScrapeInterval().getMillis(s.getScrapeInterval());
			Long nextScrape = (lastScrape+interval)-System.currentTimeMillis();
			if (nextScrape < 0) nextScrape = 0l;
			
			// + ((number++)*1000*60*60)
			Akka.system().scheduler().schedule(  
					Duration.create(nextScrape, TimeUnit.MILLISECONDS),
					Duration.create(interval, TimeUnit.MILLISECONDS),
					new Runnable() {
						@Override
						public void run() {
							try {
								if (s.getHubs() != null) {
									if (s.getHubs().contains("\t\t")) {
										s.setHubs(new HashSet<String>());
										MongoDBManager.getInstance().update(s);
									}
								}
								CrawlerControl crawler = new CrawlerControl();
								crawler.run(s, 5, 100, false);
								int k = 0;
								k ++;
							} catch (MalformedURLException e) {
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}, Akka.system().dispatcher());
		}*/
	/*	
		Akka.system().scheduler().scheduleOnce(
				Duration.create(10, TimeUnit.SECONDS),
				new Runnable() {
					public void run() {
						while(true) {
							List<Site> sites = Site.getAll();
							Site next = null;
							Long nextScrape = Long.MAX_VALUE;
							for (Site s : sites) {
								if (s.getProductPageXPath() == null) continue;
								if (s.getLastScrapedLong()+s.getScrapeInterval().getMillis(s.getScrapeInterval()) < nextScrape) {
									next = s;
									nextScrape = s.getLastScrapedLong()+s.getScrapeInterval().getMillis(s.getScrapeInterval());
								}
							}
							try {
								if (next.getHubs() != null) {
									if (next.getHubs().contains("\t\t")) {
										next.setHubs(new HashSet<String>());
										MongoDBManager.getInstance().update(next);
									}
								}
								CrawlerControl crawler = new CrawlerControl();
								crawler.run(next, 5, 10, true);
								int k = 0;
								k ++;
							} catch (MalformedURLException e) {
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				
				}, Akka.system().dispatcher());
*/
		/*try {
    	//	ImageParser.parseImages("https://www.wolfandbadger.com/drop-logo-black/", "1970'S Navy And Metallic Belted Gown - Size S");
    		//Product.removeBySite("http://www.aidacharlie.co.uk/");
    		//String t1 = "SelectS M L XL";

    	//	List<String> s = Size.getInstance().parseSizes(t1);
    /*	List<Product> p = Product.findAllBySite("http://www.philipbrownemenswear.co.uk");
    	for (int i = 0; i < p.size(); i++) {
    		String d = p.get(i).getDescription();
    		if (d.startsWith("[")) {
    		d= d.replaceAll("\",\"", ".\n");
    		d=d.replace("[\"", "");
    		d=d.replace("\"]", ".");
    		p.get(i).setDescription(d);
    		Product.update(p.get(i));
    		}
    	}*/
		/*List<Product> k = Product.findAllBySite("http://goodhoodstore.com");
    	Product.removeBySite("http://goodhoodstore.com");
    	for (int i = 0; i < k.size(); i++) {
    		String d = k.get(i).getCost();
    		double c = Double.parseDouble(d);
    		c = Math.round(c*100)/100;
    		d = c+"";
    		k.get(i).setCost(d);
    		System.out.println(d);
    		//k.get(i).updateField("cost", d);
    		//Product.delete(k.get(i).getUrl());
    		MongoDBManager.getInstance().addProduct(k.get(i));
    		//Product.update(k.get(i));

    	}*/
		/*List<Product> k = Product.findAllBySite("http://www.wolfandbadger.com");
    	Product.removeBySite("http://www.wolfandbadger.com");
    	for (int i = 0; i < k.size(); i++) {
    		List<String> f = k.get(i).getImages();
    		for (int d = 0; d < f.size(); d++) {
    			String r = f.get(d);
    			r = r.replaceAll("http://www.wolfandbadger.com", "");
    			if (r.startsWith("/")) {
    				r = "http://www.wolfandbadger.com"+r;
    			}

    			f.set(d, r);
    			System.out.println(r);
    		}
    		k.get(i).setImages(f);


    		//k.get(i).updateField("cost", d);
    		MongoDBManager.getInstance().addProduct(k.get(i));

    	}*/
		//System.out.println();
		/*	Order o = new Order();
    		Money m = new Money();
    		m.Amount = 100.0;
    		m.Currency = "GBP";
    		o.setBuyerId("jack@lasuapp.com");
    		o.setItemId("http://www.philipbrownemenswear.co.uk/adidas-originals-by-originals-x-jeremy-scott/wing-dollar-tracktop---black.aspx");
    		o.setSellerSite("http://www.philipbrownemenswear.co.uk");
    		o.setSize("L");
    		o.setAmount(m);
    		o.save();*/
		/* User user = new User();
  		  user.setUserId("amey.raut23@gmail.com");
  		  user.setProviderId("userpass");
  		  user.setEmail("amey.raut23@gmail.com");
  		  user.setFirstName("Amey");
  		  user.setMale(true);
  		  user.setName("Amey Raut");

  		  // Set up password using SecureSocial's default..BCrypt
  		  String salt = BCrypt.gensalt();
  		  String passwordHash = BCrypt.hashpw("1234", salt);
  		  user.setSalt(salt);
  		  user.setPassword(passwordHash);
  		  user.setHasher("bcrypt");
  		  user.setAuthMethod("userpass");
  		User.addUser(user);
		  Identity u = UserService.getUserIdentity(user);
		 */
		/*
  		 // Site s = Site.findByAccessCode("2306");
  		 // User us = User.findByEmail("jack@lasuapp.com");
  		 // new Payment().createMangoSite(s, us);
  		  System.out.println("Philip Brown Wallet: "+new Payment().getTotalCredit(Site.findByAccessCode("2306").getWalletId()));
  		  // TODO - check the following fields are set correctly
  		  user.setType(UserType.Consumer);
//  		  user.setParentSiteAccessCode("whatever!");
  		  Payment p = new Payment();

  		  User.addUser(user);
  		  Identity u = UserService.getUserIdentity(user);

  		  p.createMangoUser(u);*/
		// MongoDBManager.getInstance().
		/*	Site testSite = new Site();
    		testSite.setAccessCode("2222");
    		testSite.setDescription("Main-Source clothing");
    		testSite.setImageURL("http://statics.192.com/estreet/original/large/2128/21287134.jpg");
    		testSite.setName("Main Source Clothing");
    		testSite.setUrl("http://www.main-source.co.uk");
    		testSite.setWatchers(0);
    		//testSite.save();

    		Payment payTest = new Payment();
    		Money money = new Money();
    		money.Amount = 20.0;
    		money.Currency = "GBP";
    		payTest.setAmount(money);
    		payTest.setBuyer(user);
    		Product p = new Product();
    		p.setHeader("Test product");
    		payTest.setItem(p);
    		payTest.setSeller(Site.findByUrl("www.test.com"));
    		payTest.setSize("M");

    		//payTest.save();

    		Product testProduct = new Product();
    		//testProduct.setBoutique("boutique");
    		testProduct.setCost("400");
    		testProduct.setDescription("This is a test product");
    		//testProduct.setDesigner("designer");
    		//testProduct.setId("000");
    		testProduct.setImages(Arrays.asList(new String[]{"/images/favicon"}));
    		testProduct.setHeader("Test Product 2");
    		testProduct.setSite("www.test.com");
    		testProduct.setUrl("www.test.com/test");


    		//MongoDBManager.getInstance().addSite(testSite);
    		MongoDBManager.getInstance().addProduct(testProduct);
		 */
		/*Lasu lasu = new Lasu();
    		lasu.mangoId = Payment.getId();
    		lasu.walletId = Payment.getWalletId();
    		MongoDBManager.getInstance().addLasuConfig(lasu);*/
		//MongoDBManager.getInstance().dropProducts();
		//MongoDBManager.getInstance().dropUsers();
		/*removeTestOrders("http://www.themercantilelondon.com");
    		createTestOrder("jack@lasuapp.com", "http://www.themercantilelondon.com");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}