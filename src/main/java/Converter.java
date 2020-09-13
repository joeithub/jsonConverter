import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: tongq
 * @Date: 2020/9/3 18:54
 * @since：0.0.1
 */
public class Converter {
        public static void main(String[] args) {
            Frame f = new Frame("JSON TO CSV TOOL ");

            f.setBounds(400, 200, 400, 440);
            //选择布局方式
            f.setLayout(new FlowLayout());

            //创建文本框
            TextField tf = new TextField(48);

            TextField notice = new TextField(48);

            notice.setEditable(false);
            notice.setFocusable(false);

            //创建按钮
            Button bu = new Button("Convert Single File");
            Button all = new Button("Convert All JSON File TO ONE");

            //创建文本域
            TextArea ta = new TextArea("",20, 49,TextArea.SCROLLBARS_NONE);
            ta.setEditable(false);
            ta.setFocusable(false);


            f.add(tf);
            f.add(bu);
            f.add(all);
            f.add(notice);
            f.add(ta);


            String state =      "*********************** json to csv ************************\n"+
                                "版本： V0.0.1\n" +
                                "作者： 童峤\n" +
                                "使用： 输入json 文件所在目录地址，csv 文件会生成到同目录下\n" +
                                "注意： 当前仅支持一层json 转换为csv,不支持复杂嵌套结构\n"  +
                                "示例：\n" +
                                "[\n" +
                                "      {\n" +
                                "        \"_index\" : \"res-filebeat-2020.06.18\",\n" +
                                "        \"url\" : \"edu.cn/jsxsd/css/images/up.png\",\n" +
                                "        \"account\" : \"1171413527\"\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"_index\" : \"res-filebeat-2020.06.18\",\n" +
                                "        \"url\" : \"edu.cn/jsxsd/xspj/xspj_find.do\",\n" +
                                "        \"account\" : \"1171413527\"\n" +
                                "      }\n"+
                                "]\n"+
                                "************************ 2020-09-03 ************************";
            ta.setText(state);
            notice.setText("请输入文件夹路径");
            f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

            //给按钮添加实践
            all.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    //获取文本框中的文本
                    String tf_str = tf.getText().trim();
                    if (StringUtils.isEmpty(tf_str)){
                        notice.setText("路径不能为空");
                        notice.setForeground(Color.red);
                        return;
                    }
                    try {
                        if (! checkPath(tf_str, notice)){
                            return;
                        }
                        FileUtils.jsonToCsv(tf_str,true);
                        notice.setForeground(Color.green);
                        notice.setText("转换成功 请到文件目录查看");
                    } catch (Exception e1) {
                        notice.setForeground(Color.red);
                        notice.setText("转换失败 json格式错误");
                        e1.printStackTrace();
                    }
                    //将光标移动到tf文本框
                    tf.requestFocus();
                }
            });

            //给按钮添加实践
            bu.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    //获取文本框中的文本
                    String tf_str = tf.getText().trim();
                    if (StringUtils.isEmpty(tf_str)){
                        notice.setForeground(Color.red);
                        notice.setText("路径不能为空");
                        return;
                    }
                    try {
                        if (! checkPath(tf_str, notice)){
                            return;
                        }
                        FileUtils.jsonToCsv(tf_str,false);
                        notice.setForeground(Color.green);
                        notice.setText("转换成功 请到文件目录查看");
                    } catch (Exception e1) {
                        notice.setForeground(Color.red);
                        notice.setText("转换失败 json格式错误");
                        e1.printStackTrace();
                    }
                    //将光标移动到tf文本框
                    tf.requestFocus();
                }
            });

            //设置窗体显示
            f.setVisible(true);
        }

    private static boolean checkPath(String tf_str, TextField notice) {
        File file = new File(tf_str);
        if (! file.exists() && ! file.isDirectory()){
            notice.setForeground(Color.red);
            notice.setText("文件路径不存在");
            return false;
        }
        File[] files = file.listFiles();
        List<File> collect = Arrays.stream(files).filter(x -> x.getName().endsWith(".json")).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect) || collect.size() == 0){
            System.out.println(collect.size());
            notice.setForeground(Color.red);
            notice.setText("路径下没有json文件");
            return false;
        }
        return true;
    }
}
