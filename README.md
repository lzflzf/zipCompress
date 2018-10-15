# zipCompress
使用apache common 工具实现zip压缩


参考1：https://wenku.baidu.com/view/3e635e44b94ae45c3b3567ec102de2bd9605def1.html

参考2：https://www.aliyun.com/jiaocheng/788130.html

之所以重复造轮子

1： 参考1中的教授使用了递归进行压缩，但是递归一般情况下是挺耗资源的和羞涩难懂的。

2： 没法知道压缩的进度

3:   空文件没有进行处理

4： 参考2中只实现的单个文件和多个文件的压缩，并没有实现文件夹的压缩，但已经是挺完美的了，起码一眼就能看懂。

