package com.github.boyxie.mybatis.plugins;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.util.StringUtility;
import static org.mybatis.generator.internal.util.StringUtility.isTrue;

/**
 * mybatis generator 自定义comment生成器.
 * 基于MBG 1.3.5.
 *
 * @author boyxie 2017-02-17
 */
public class MyCommentGenerator implements CommentGenerator {

    private Properties properties;
    private Properties systemPro;
    private boolean suppressDate;
    private boolean suppressAllComments;
    private boolean addRemarkComments;
    private SimpleDateFormat dateFormat;

    public MyCommentGenerator() {
        super();
        properties = new Properties();
        systemPro = System.getProperties();
        suppressDate = false;
        suppressAllComments = false;
    }

    public void addJavaFileComment(CompilationUnit compilationUnit) {
        // add no file level comments by default
        return;
    }

    /**
     * the xml element
     */
    public void addComment(XmlElement xmlElement) {
        return;
    }

    public void addRootComment(XmlElement rootElement) {
        // add no document level comments by default
        return;
    }

    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);

        suppressDate = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));

        suppressAllComments = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));

        addRemarkComments = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_ADD_REMARK_COMMENTS));

        String dateFormatString = properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_DATE_FORMAT);
        if (StringUtility.stringHasValue(dateFormatString)) {
            dateFormat = new SimpleDateFormat(dateFormatString);
        }
    }

    /**
     * 生成通用注释
     *
     * @param javaElement the java element
     */
    protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
//        javaElement.addJavaDocLine(" *");
        StringBuilder sb = new StringBuilder();
        sb.append(" *   @author ");
        sb.append(properties.getProperty("author"));
//        sb.append(systemPro.getProperty("user.name"));
//        sb.append(MergeConstants.NEW_ELEMENT_TAG);
        if (markAsDoNotDelete) {
            sb.append(" do_not_delete_during_merge");
        }
        javaElement.addJavaDocLine(" * ");
        String s = getDateString();
        if (s != null) {
            sb.append(' ');
            sb.append(s);
        }
        javaElement.addJavaDocLine(sb.toString());
    }

    /**
     * @return a string representing the current timestamp, or null
     */
    protected String getDateString() {
        if (suppressDate) {
            return null;
        } else if (dateFormat != null) {
            return dateFormat.format(new Date());
        } else {
            return new Date().toString();
        }
    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        innerClass.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        innerClass.addJavaDocLine(sb.toString());

        addJavadocTag(innerClass, false);

        innerClass.addJavaDocLine(" */");
    }

    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        innerEnum.addJavaDocLine("/**");
//        addJavadocTag(innerEnum, false);
        sb.append(" * ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        innerEnum.addJavaDocLine(sb.toString());
        innerEnum.addJavaDocLine(" */");
    }

    /**
     * 属性字段注释
     */
    public void addFieldComment(Field field, IntrospectedTable introspectedTable,
                                IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        field.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(introspectedColumn.getRemarks());
        field.addJavaDocLine(sb.toString());

        //      addJavadocTag(field, false);

        field.addJavaDocLine(" */");
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        field.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        field.addJavaDocLine(sb.toString());
        field.addJavaDocLine(" */");
    }

    /**
     * 类的数据库表的注释
     */
    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (suppressAllComments || !addRemarkComments) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        topLevelClass.addJavaDocLine("/**"); //$NON-NLS-1$

        String remarks = introspectedTable.getRemarks();
        if (addRemarkComments && StringUtility.stringHasValue(remarks)) {
//            topLevelClass.addJavaDocLine(" * ");
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));  //$NON-NLS-1$
            for (String remarkLine : remarkLines) {
                topLevelClass.addJavaDocLine(" *   " + remarkLine);  //$NON-NLS-1$
            }
        }
        sb.append(" *   "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable());
        topLevelClass.addJavaDocLine(sb.toString());

        addJavadocTag(topLevelClass, false);

        topLevelClass.addJavaDocLine(" */"); //$NON-NLS-1$
    }

    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }
        method.addJavaDocLine("/**");
        switch (method.getName()) {
            case "toString":
                method.addJavaDocLine(" * 重写toString");
                break;
            case "deleteByPrimaryKey":
                method.addJavaDocLine(" * 根据主键删除对象");
                break;
            case "insertSelective":
                method.addJavaDocLine(" * 新增对象字段时判空");
                break;
            case "insert":
                method.addJavaDocLine(" * 新增对象");
                break;
            case "updateByPrimaryKeySelective":
                method.addJavaDocLine(" * 根据主键更新对象字段时判空");
                break;
            case "updateByPrimaryKey":
                method.addJavaDocLine(" * 根据主键更新对象");
                break;
            case "selectByPrimaryKey":
                method.addJavaDocLine(" * 根据主键查询对象");
                break;
            default:break;
        }
//        addJavadocTag(method, false);
        method.addJavaDocLine(" */");
    }

    /**
     * getter属性字段注释
     */
    public void addGetterComment(Method method, IntrospectedTable introspectedTable,
                                 IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }

//        method.addJavaDocLine("/**");
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(" * ");
//        sb.append(introspectedColumn.getRemarks());
//        method.addJavaDocLine(sb.toString());
//
//        sb.setLength(0);
//        sb.append(" * @return ");
//        sb.append(introspectedColumn.getActualColumnName());
//        sb.append(" ");
//        sb.append(introspectedColumn.getRemarks());
//        method.addJavaDocLine(sb.toString());
//
//        //      addJavadocTag(method, false);
//
//        method.addJavaDocLine(" */");
    }

    /**
     * setter属性字段注释
     */
    public void addSetterComment(Method method, IntrospectedTable introspectedTable,
                                 IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }


//        method.addJavaDocLine("/**");
//        StringBuilder sb = new StringBuilder();
//        sb.append(" * ");
//        sb.append(introspectedColumn.getRemarks());
//        method.addJavaDocLine(sb.toString());
//
//        Parameter parm = method.getParameters().get(0);
//        sb.setLength(0);
//        sb.append(" * @param ");
//        sb.append(parm.getName());
//        sb.append(" ");
//        sb.append(introspectedColumn.getRemarks());
//        method.addJavaDocLine(sb.toString());
//
//        //      addJavadocTag(method, false);
//
//        method.addJavaDocLine(" */");
    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
        if (suppressAllComments) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        innerClass.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        innerClass.addJavaDocLine(sb.toString());

        addJavadocTag(innerClass, markAsDoNotDelete);

        innerClass.addJavaDocLine(" */");

    }

    public static void main(String[] args) {
        try {
            List<String> warnings = new ArrayList<String>();
            boolean overwrite = true;
            File configFile = new File("/Users/jianxunxie/myIdea/git/sweet-demo/demo/src/main/resources/generatorConfig.xml");
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(configFile);
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
