package com.mycompany.sistemaintegramind.util.Utilitarios;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class CircularIconLabelBeanInfo extends SimpleBeanInfo {

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            // Criando os descritores para as suas propriedades personalizadas
            PropertyDescriptor customCircleSize = new PropertyDescriptor("customCircleSize", CircularIconLabel.class);
            PropertyDescriptor iconColor = new PropertyDescriptor("iconColor", CircularIconLabel.class);
            PropertyDescriptor iconHeight = new PropertyDescriptor("iconHeight", CircularIconLabel.class);
            PropertyDescriptor iconWidth = new PropertyDescriptor("iconWidth", CircularIconLabel.class);
            PropertyDescriptor iconOffsetX = new PropertyDescriptor("iconOffsetX", CircularIconLabel.class);
            PropertyDescriptor iconOffsetY = new PropertyDescriptor("iconOffsetY", CircularIconLabel.class);
            PropertyDescriptor shadowBlur = new PropertyDescriptor("shadowBlur", CircularIconLabel.class);
            PropertyDescriptor shadowColor = new PropertyDescriptor("shadowColor", CircularIconLabel.class);
            PropertyDescriptor shadowOffsetX = new PropertyDescriptor("shadowOffsetX", CircularIconLabel.class);
            PropertyDescriptor shadowOffsetY = new PropertyDescriptor("shadowOffsetY", CircularIconLabel.class);

            PropertyDescriptor[] props = new PropertyDescriptor[]{
                customCircleSize, iconColor, iconWidth, iconHeight, iconOffsetX, iconOffsetY,
                shadowColor, shadowBlur, shadowOffsetX, shadowOffsetY
            };

            for (PropertyDescriptor pd : props) {
                // 1. OBRIGA a ir para a aba PRINCIPAL de "Propriedades" em vez de "Outras"
                pd.setPreferred(true); 
            }

            // [OPCIONAL] TRUQUE PARA FORÇAR FICAREM NO TOPO:
            // O NetBeans ordena por ordem alfabética. Se você quiser que elas fiquem
            // presas no topo absoluto da lista, descomente as linhas abaixo para renomear
            // como elas aparecem visualmente no NetBeans (isso não muda o nome no código):
            
            /*
            customCircleSize.setDisplayName("01. Tamanho do Círculo");
            iconColor.setDisplayName("02. Cor do Ícone");
            iconWidth.setDisplayName("03. Largura do Ícone");
            iconHeight.setDisplayName("04. Altura do Ícone");
            iconOffsetX.setDisplayName("05. Offset X do Ícone");
            iconOffsetY.setDisplayName("06. Offset Y do Ícone");
            shadowColor.setDisplayName("07. Cor da Sombra");
            shadowBlur.setDisplayName("08. Desfoque da Sombra");
            shadowOffsetX.setDisplayName("09. Offset X da Sombra");
            shadowOffsetY.setDisplayName("10. Offset Y da Sombra");
            */

            return props;
        } catch (IntrospectionException e) {
            return null;
        }
    }

    // ISSO É MUITO IMPORTANTE: 
    // Diz para o NetBeans não apagar as propriedades originais do JLabel (text, background, font...)
    @Override
    public BeanInfo[] getAdditionalBeanInfo() {
        try {
            return new BeanInfo[]{Introspector.getBeanInfo(javax.swing.JLabel.class)};
        } catch (IntrospectionException e) {
            return null;
        }
    }
}