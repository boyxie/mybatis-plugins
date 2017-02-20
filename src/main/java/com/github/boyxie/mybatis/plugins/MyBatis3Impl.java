package com.github.boyxie.mybatis.plugins;

import java.text.MessageFormat;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

//MyBatis3 的实现类的自定义命名
public class MyBatis3Impl extends IntrospectedTableMyBatis3Impl {
    @Override
    protected String calculateMyBatis3XmlMapperFileName() {
        StringBuilder sb = new StringBuilder();
        if (stringHasValue(tableConfiguration.getMapperName())) {
            String mapperName = tableConfiguration.getMapperName();
            int ind = mapperName.lastIndexOf('.');
            if (ind == -1) {
                sb.append(mapperName);
            } else {
                sb.append(mapperName.substring(ind + 1));
            }
            //支持mapperName = "{0}Dao" 等用法
            sb.append(MessageFormat.format(mapperName, fullyQualifiedTable.getDomainObjectName()));
            sb.append(".xml"); //$NON-NLS-1$
        } else {
            sb.append(fullyQualifiedTable.getDomainObjectName());
            sb.append("Mapper.xml"); //$NON-NLS-1$
        }
        return sb.toString();
    }

    @Override
    protected void calculateJavaClientAttributes() {
        if (context.getJavaClientGeneratorConfiguration() == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(calculateJavaClientImplementationPackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("DAOImpl"); //$NON-NLS-1$
        setDAOImplementationType(sb.toString());

        sb.setLength(0);
        sb.append(calculateJavaClientInterfacePackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("DAO"); //$NON-NLS-1$
        setDAOInterfaceType(sb.toString());

        sb.setLength(0);
        sb.append(calculateJavaClientInterfacePackage());
        sb.append('.');
        if (stringHasValue(tableConfiguration.getMapperName())) {
            //支持mapperName = "{0}Dao" 等用法
            sb.append(MessageFormat.format(tableConfiguration.getMapperName(), fullyQualifiedTable.getDomainObjectName()));
        } else {
            sb.append(fullyQualifiedTable.getDomainObjectName());
            sb.append("Mapper"); //$NON-NLS-1$
        }
        setMyBatis3JavaMapperType(sb.toString());

        sb.setLength(0);
        sb.append(calculateJavaClientInterfacePackage());
        sb.append('.');
        if (stringHasValue(tableConfiguration.getSqlProviderName())) {
            //支持mapperName = "{0}SqlProvider" 等用法
            sb.append(MessageFormat.format(tableConfiguration.getSqlProviderName(), fullyQualifiedTable.getDomainObjectName()));
        } else {
            sb.append(fullyQualifiedTable.getDomainObjectName());
            sb.append("SqlProvider"); //$NON-NLS-1$
        }
        setMyBatis3SqlProviderType(sb.toString());
    }
}
