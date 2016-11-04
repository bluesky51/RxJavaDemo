package net.sourceforge.simcpux.rxjavademo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import net.sourceforge.simcpux.rxjavademo.HttpUtils.HttpUtils;
import net.sourceforge.simcpux.rxjavademo.cache.MyDiskCache;
import net.sourceforge.simcpux.rxjavademo.cache.MyMemoryCache;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    String result = "";
    int count = 0;
    Observable obs;
    Subscriber s;

    MyMemoryCache myMemoryCache;
    MyDiskCache myDiskCache;

    String url = "http://scimg.jb51.net/allimg/160924/103-160924125P3526.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        myMemoryCache = MyMemoryCache.getMyMemeryCache();
        myDiskCache = MyDiskCache.getMyDiskCache(this);
        concatAndFirstOperation();

    }

    //入门使用1
    public void baseUseRxjava1() {
        //        //创建Observable被观察者对象
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext("Hello, world!");
                        sub.onCompleted();
                    }
                }
        );
//        //创建一个观察者
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e("=====", "====完成==");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e("=====", "====输出内容为==" + s);

            }
        };
//        //完成subscriber对observable的订阅
//        /**
//         * 一旦mySubscriber订阅了myObservable，
//         * myObservable就是调用mySubscriber对象的onNext和onComplete方法，
//         * mySubscriber就会打印出Hello World！
//         */
        myObservable.subscribe(subscriber);
//
    }

    //入门使用2
    public void baseUseRxjava2() {
//        //简化方式
        Observable.just("哈哈哈")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e("=====", "====callgggg输出内容为==" + s);
                    }
                });
        //使用范式完成
        Observable
                .just("哈哈哈")
                .subscribe(s ->
                        Log.e("=====", "====callfff输出内容为==" + s));
    }

    //入门使用3
    public void baseUseRxjava3() {
        String[] strings = new String[]{"语文", "英语", "数学"};
        Observable.from(strings).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e("=======", "===onNext======" + s);
            }
        });
//
    }

    //map操作符的使用
    public void useMapOperation() {
        //map操作符
//        //案例1
//        Observable.just("嘿嘿嘿").map(s ->
//                Log.e("=====", "====map输出内容为==" + s))
//                .subscribe(s -> Log.e("=====", s + "====Action1输出内容为看看买了两厘米=="));
//        //案例2
//        //刚创建的Observable是String类型的
//        Observable.just("Hellp Map Operator")
//                .map(new Func1<String, Integer>() {
//                    @Override
//                    public Integer call(String s) {
//                        Log.e("=====", "=第一个map转化==");
//                        return 2016;
//                    }
//                }).map(new Func1<Integer, String>() {
//            @Override
//            public String call(Integer integer) {
//                Log.e("=====", "=第二个map转化=");
//                return String.valueOf(integer);
//            }
//        }).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                Log.e("=====", "=最终结果=" + s);
//            }
//        });
//
//        //范式形式如下：
//        Observable.just("Hellp Map Operator")
//                //第一次转化
//                .map(s -> "2016")
//                //第二次转化
//                .map(m -> 2016)
//                //最终结果发给观察者
//                .subscribe(k -> Log.e("=====", "=最终结果=" + k));


        //案例3
        Observable.just(Environment.getExternalStorageDirectory() + "/DCIM/Camera/aa.jpg") // 输入类型 String
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String filePath) { // 参数类型 String
                        return BitmapFactory.decodeFile(filePath); // 返回类型 Bitmap
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) { // 参数类型 Bitmap
                        imageView.setImageBitmap(bitmap);
                    }
                });

    }

    //flatMap操作符的使用
    public void useFlatMapOperation() {

        Student[] students = new Student[5];
        for (int i = 0; i < 5; i++) {
            Student s = new Student();
            s.setName("张三" + i);
            List<Course> list = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                Course c = new Course();
                c.setCourseName("科目" + i);
                list.add(c);
            }
            s.setCourseList(list);
            students[i] = s;

        }
//        Observable.from(students)
//                .map(new Func1<Student, String>() {
//                    @Override
//                    public String call(Student student) {
//                        return student.getName();
//                    }
//                })
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        Log.e("======", "===名字是===" + s);
//                    }
//                });
//
//        Observable.from(students).subscribe(new Subscriber<Student>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Student student) {
//                List<Course> courses = student.getCourseList();
//                for (int i = 0; i < courses.size(); i++) {
//                    Course course = courses.get(i);
//                    // Log.e("====选课的名称==", course.getCourseName());
//                }
//            }
//        });
///**
// *  flatMap() 的原理是这样的：
// *          1. 使用传入的事件对象创建一个 Observable 对象；
// *          2. 并不发送这个 Observable, 而是将它激活，于是它开始发送事件；
// *          3. 每一个创建出来的 Observable 发送的事件，都被汇入同一个 Observable ，
// *          而这个 Observable 负责将这些事件统一交给 Subscriber 的回调方法。
// *          这三个步骤，把事件拆成了两级，通过一组新创建的 Observable 将初始的对象
// *          『铺平』之后通过统一路径分发了下去。而这个『铺平』就是 flatMap() 所谓的 flat。
// */
        Observable.from(students).flatMap(new Func1<Student, Observable<List<Course>>>() {
            @Override
            public Observable<List<Course>> call(Student student) {
                return Observable.just(student.getCourseList());
            }
        }).subscribe(new Subscriber<List<Course>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Course> courses) {
                for (int i = 0; i < courses.size(); i++) {
                    Course course = courses.get(i);
                    Log.e("====选课的名称gggg==", course.getCourseName());
                }
            }
        });
    }

    //线程调度相关案例
    public void threadSchedulers() {
//        //Schedulers线程调度；
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(num -> Log.e("===Schedulers线程调度===", "number:" + num));
//

        Observable.just(1, 2, 3, 4) // IO 线程，由 subscribeOn() 指定
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(new Func1<Integer, Object>() {

                    @Override
                    public Object call(Integer integer) {
                        return integer + 4;
                    }
                }) // 新线程，由 observeOn() 指定
                .observeOn(Schedulers.io())
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object o) {
                        return (Integer) o + 2;
                    }
                }) // IO 线程，由 observeOn() 指定
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {
                        Log.e("=====", "===onNext====" + o);
                    }
                });  // Android 主线程，由 observeOn() 指定
    }

    //merge合并多个数据源
    public void mergeData() {
        Observable<String> ob1 = Observable.just("哈哈哈");
        Observable<String> ob2 = Observable.just("呵呵呵");
        Observable.merge(ob1, ob2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("=====", "done loading all data");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("=====", "error");
                    }

                    @Override
                    public void onNext(String data) {
                        Log.e("=====", "all merged data will pass here one by one!");
                        result += data;
                        count++;
                        if (count == 2) {
                            Log.e("=====", "==合并后最终的到的数据是=" + result);
                        }

                    }
                });
    }

    //使用timer做定时操作。表示“指定秒数后执行指定操作”
    public void timer() {
        //表示2s后输出内容
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        Log.e("======", "完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("======", "失败" + e.getMessage());
                    }

                    @Override
                    public void onNext(Long number) {
                        Log.e("======", "哈哈哈哈哈哈哈哈哈哈");
                    }
                });
    }

    //使用interval做周期性操作,表示每隔指定数执行指定的操作
    public void intervalDeliver() {
        obs = Observable.interval(2, TimeUnit.SECONDS);
        s = new Subscriber() {
            @Override
            public void onCompleted() {
                Log.e("======", "完成");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("======", "失败原因是：" + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                Log.e("======", "哈哈哈哈哈");


            }
        };
        //使得观察者s观察被观察者obs
        obs.subscribe(s);
    }

    //使用schedulePeriodically做轮询请求
    public void scheduleTask() {
        //直接进行网络请求，本身就是异步的，不需要开启线程
        Schedulers.newThread().createWorker()
                .schedulePeriodically(new Action0() {
                    @Override
                    public void call() {
                        //网络请求得到结果让观察者观察到进行下一步处理
                        //doNetworkCallAndGetStringResult()
                        InputStream is = HttpUtils.getInputStream("http://www.baidu.com/");
                        String str = HttpUtils.getDataByInputStream(is);
                        Log.e("=====", "====str===" + str);
                    }
                    //参数1:初始事件，参数2:间隔或者轮询时间
                }, 1000, 1000, TimeUnit.MILLISECONDS);


////       // 使用Rxjava模式进行轮询请求
//        Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(final Subscriber<? super String> observer) {
//
//                Schedulers.newThread().createWorker()
//                        .schedulePeriodically(new Action0() {
//                            @Override
//                            public void call() {
//                                //网络请求得到结果让观察者观察到进行下一步处理
//                                //doNetworkCallAndGetStringResult()
//                                InputStream is = HttpUtils.getInputStream("http://www.baidu.com/");
//                                String str = HttpUtils.getDataByInputStream(is);
//                                observer.onNext(str);
//                            }
//                            //参数1:初始事件，参数2:间隔或者轮询时间
//                        }, 1000, 1000, TimeUnit.MILLISECONDS);
//            }
//        }).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                Log.e("====", "======" + s);
//            }
//        });
    }

    //使用concat和first做三级缓存
    public void concatAndFirstOperation(){
   //依次检查memory、disk和network中是否存在数据，任何一步一旦发现数据后面的操作都不执行。
        // 检查memory
        Observable<Bitmap> memory = Observable.create(new Observable.OnSubscribe<Bitmap>() {
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bmp = myMemoryCache.get(url);
                if (bmp != null) {
                    subscriber.onNext(bmp);
                    Log.e("=====", "==memory==onNext=");
                } else {
                    subscriber.onCompleted();
                    Log.e("=====", "==memory==onCompleted=");
                }
            }
        });
        //检查disk
        Observable<Bitmap> disk = Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bmp = myDiskCache.getBitmap(url);
                if (bmp != null) {
                    subscriber.onNext(bmp);
                    Log.e("=====", "==disk==onNext=");
                } else {
                    subscriber.onCompleted();
                    Log.e("=====", "==disk==onCompleted=");
                }
            }
        });
        //网络执行
        Observable<Bitmap> network = Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Schedulers.newThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        InputStream is = HttpUtils.getInputStream(url);
                        Bitmap bmp = BitmapFactory.decodeStream(is);
                        myMemoryCache.put(url, bmp);
                        myDiskCache.putBitmap(url, bmp);
                        if (bmp != null) {
                            Log.e("=====", "==network==onNext=");
                            subscriber.onNext(bmp);
                        } else {
                            Log.e("=====", "==network==onCompleted=");
                            subscriber.onCompleted();
                        }
                    }
                });
            }
        });
         //观察者
        Subscriber s = new Subscriber<Bitmap>() {

            @Override
            public void onCompleted() {
                Log.e("=====", "==Subscriber==onCompleted=");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("=====", "==Subscriber==onError=" + e.getMessage());
            }

            @Override
            public void onNext(Bitmap bitmap) {
                Log.e("=====", "==Subscriber==onNext=");
                imageView.setImageBitmap(bitmap);

            }
        };
        //依次检查memory、disk、network
        Observable.concat(memory, disk, network)
                .first()
                //事件发生在子线程
                .subscribeOn(Schedulers.newThread())
                //事件消费发生在主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //页面结束,移除观察者
        if (obs != null && s != null) {
            Subscription subscription = obs.unsafeSubscribe(s);
            subscription.unsubscribe();
        }
    }
}
